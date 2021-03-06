/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.BlockModFlower;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockBuriedPetals extends BlockModFlower {

	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 1.6, 16);

	public BlockBuriedPetals(DyeColor color, Properties builder) {
		super(color, builder);
	}

	@Nonnull
	@Override
	@OnlyIn(Dist.CLIENT)
	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.NONE;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		int hex = color.colorValue;
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		SparkleParticleData data = SparkleParticleData.noClip(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
		world.addParticle(data, pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.1 + rand.nextFloat() * 0.1, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

}
