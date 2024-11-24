package net.satisfy.vinery.core.block.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.vinery.core.registry.StorageTypeRegistry;
import org.jetbrains.annotations.NotNull;

import static net.satisfy.vinery.core.registry.ObjectRegistry.*;

public class WineBoxBlock extends StorageBlock {

    private static final VoxelShape SHAPE_S = makeShapeS();
    private static final VoxelShape SHAPE_E = makeShapeE();

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public WineBoxBlock(Properties settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case WEST, EAST -> SHAPE_E;
            default -> SHAPE_S;
        };
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && stack.isEmpty()) {
            if(!world.isClientSide()){
                world.setBlock(pos, state.setValue(OPEN, !state.getValue(OPEN)), UPDATE_ALL);
            }
            return InteractionResult.sidedSuccess(world.isClientSide());
        }
        else if(state.getValue(OPEN)){
            return super.use(state, world, pos, player, hand, hit);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }

    @Override
    public boolean canInsertStack(ItemStack stack) {
        return stack.getItem() == CHERRY_WINE_ITEM.get() ||
                stack.getItem() == RED_WINE_ITEM.get() ||
                stack.getItem() == STAL_WINE_ITEM.get() ||
                stack.getItem() == BOLVAR_WINE_ITEM.get() ||
                stack.getItem() == SOLARIS_WINE_ITEM.get() ||
                stack.getItem() == KELP_CIDER_ITEM.get() ||
                stack.getItem() == CLARK_WINE_ITEM.get() ||
                stack.getItem() == BOTTLE_MOJANG_NOIR_ITEM.get() ||
                stack.getItem() == VILLAGERS_FRIGHT_ITEM.get() ||
                stack.getItem() == NOIR_WINE_ITEM.get();
    }

    @Override
    public Direction[] unAllowedDirections() {
        return new Direction[]{Direction.DOWN, Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};
    }

    @Override
    public int size(){
        return 1;
    }

    @Override
    public ResourceLocation type() {
        return StorageTypeRegistry.WINE_BOX;
    }

    @Override
    public int getSection(Float x, Float y) {
        return 0;
    }



    private static VoxelShape makeShapeS() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0625, 0, 0.3125, 0.09375, 0.3125, 0.6875), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.90625, 0, 0.3125, 0.9375, 0.3125, 0.6875), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.09375, 0, 0.3125, 0.90625, 0.3125, 0.34375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.09375, 0.03125, 0.34375, 0.90625, 0.09375, 0.65625), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.09375, 0, 0.65625, 0.90625, 0.3125, 0.6875), BooleanOp.OR);

        return shape;
    }

    private static VoxelShape makeShapeE() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.3125, 0, 0.0625, 0.6875, 0.3125, 0.09375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.3125, 0, 0.90625, 0.6875, 0.3125, 0.9375), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.65625, 0, 0.09375, 0.6875, 0.3125, 0.90625), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.34375, 0.03125, 0.09375, 0.65625, 0.09375, 0.90625), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.3125, 0, 0.09375, 0.34375, 0.3125, 0.90625), BooleanOp.OR);

        return shape;
    }
}