package com.alchemybag;

import cn.lchnn.hugestorage.HugeStorageMenu;
import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

public final class AlchemyBagMod implements ModInitializer {
    public static final String MOD_ID = "alchemybag";
    public static final int CAPACITY = 810;
    public static final Item[] BAGS = new Item[16];
    public static final DataComponentType<BagContents> BAG_CONTENTS = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        id("contents"),
        DataComponentType.<BagContents>builder().persistent(BagContents.CODEC).build()
    );

    @Override
    public void onInitialize() {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName() + "_alchemical_bag";
            Identifier identifier = id(name);
            ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
            Item.Properties properties = new Item.Properties()
                .setId(key)
                .stacksTo(1)
                .component(BAG_CONTENTS, BagContents.EMPTY);
            BAGS[color.getId()] = Registry.register(BuiltInRegistries.ITEM, key, new AlchemicalBagItem(properties));
        }
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static final class AlchemicalBagItem extends Item {
        public AlchemicalBagItem(Properties properties) {
            super(properties);
        }

        @Override
        public InteractionResult use(Level level, Player player, InteractionHand hand) {
            ItemStack bag = player.getItemInHand(hand);
            if (!level.isClientSide()) {
                BagContainer contents = new BagContainer(bag);
                player.openMenu(new SimpleMenuProvider(
                    (containerId, inventory, ignored) -> HugeStorageMenu.createSingleServer(containerId, inventory, contents),
                    bag.getHoverName()
                ));
            }
            return InteractionResult.SUCCESS;
        }
    }

    public record BagContents(List<StoredSlot> slots) {
        public static final BagContents EMPTY = new BagContents(List.of());
        public static final Codec<BagContents> CODEC = StoredSlot.CODEC
            .sizeLimitedListOf(CAPACITY)
            .xmap(BagContents::new, BagContents::slots);

        public BagContents {
            slots = List.copyOf(slots);
        }

        public static BagContents fromStacks(List<ItemStack> stacks) {
            List<StoredSlot> stored = new ArrayList<>();
            for (int slot = 0; slot < stacks.size(); slot++) {
                ItemStack stack = stacks.get(slot);
                if (!stack.isEmpty()) stored.add(new StoredSlot(slot, ItemStackTemplate.fromNonEmptyStack(stack)));
            }
            return stored.isEmpty() ? EMPTY : new BagContents(stored);
        }

        public void copyInto(NonNullList<ItemStack> destination) {
            for (StoredSlot stored : slots) {
                if (stored.slot() < destination.size()) destination.set(stored.slot(), stored.item().create());
            }
        }
    }

    public record StoredSlot(int slot, ItemStackTemplate item) {
        public static final Codec<StoredSlot> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(0, CAPACITY - 1).fieldOf("slot").forGetter(StoredSlot::slot),
            ItemStackTemplate.CODEC.fieldOf("item").forGetter(StoredSlot::item)
        ).apply(instance, StoredSlot::new));
    }

    private static final class BagContainer extends SimpleContainer {
        private final ItemStack bag;

        private BagContainer(ItemStack bag) {
            super(CAPACITY);
            this.bag = bag;
            BagContents contents = bag.get(BAG_CONTENTS);
            if (contents != null) {
                contents.copyInto(items);
            } else {
                bag.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
                bag.remove(DataComponents.CONTAINER);
                setChanged();
            }
        }

        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return !(stack.getItem() instanceof AlchemicalBagItem);
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            if (!(stack.getItem() instanceof AlchemicalBagItem)) super.setItem(slot, stack);
        }

        @Override
        public void setChanged() {
            bag.set(BAG_CONTENTS, BagContents.fromStacks(items));
        }
    }
}
