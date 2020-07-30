/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.ComparatorMode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.item.IBurstViewerBauble;
import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.client.core.handler.BaubleRenderHandler;
import vazkii.botania.client.core.helper.I18NHelper;
import vazkii.botania.common.core.handler.EquipmentHandler;

public class ItemMonocle extends ItemBauble implements IBurstViewerBauble, ICosmeticBauble {

	public ItemMonocle(Properties props) {
		super(props);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(BaubleRenderHandler layer, ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		layer.getEntityModel().bipedHead.translateRotate(ms);
		ms.translate(0.15, -0.2, -0.25);
		ms.scale(0.3F, -0.3F, -0.3F);
		Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, ms, buffers);
	}

	@OnlyIn(Dist.CLIENT)
	public static void renderHUD(PlayerEntity player) {
		Minecraft mc = Minecraft.getInstance();
		RayTraceResult ray = mc.objectMouseOver;
		if (ray == null || ray.getType() != RayTraceResult.Type.BLOCK) {
			return;
		}
		BlockPos pos = ((BlockRayTraceResult) ray).getPos();
		BlockState state = player.world.getBlockState(pos);
		Block block = state.getBlock();
		player.world.getTileEntity(pos);

		ItemStack dispStack = ItemStack.EMPTY;
		String text = "";

		if (block == Blocks.REDSTONE_WIRE) {
			dispStack = new ItemStack(Items.REDSTONE);
			text = TextFormatting.RED + I18NHelper.formatInteger(state.get(RedstoneWireBlock.POWER));
		} else if (block == Blocks.REPEATER) {
			dispStack = new ItemStack(Blocks.REPEATER);
			text = I18NHelper.formatInteger(state.get(RepeaterBlock.DELAY));
		} else if (block == Blocks.COMPARATOR) {
			dispStack = new ItemStack(Blocks.COMPARATOR);
			text = state.get(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT ? "-" : "+";
		}

		if (dispStack.isEmpty()) {
			return;
		}

		int x = mc.getMainWindow().getScaledWidth() / 2 + 15;
		int y = mc.getMainWindow().getScaledHeight() / 2 - 8;

		mc.getItemRenderer().renderItemAndEffectIntoGUI(dispStack, x, y);

		mc.fontRenderer.drawStringWithShadow(text, x + 20, y + 4, 0xFFFFFF);
	}

	public static boolean hasMonocle(PlayerEntity player) {
		return EquipmentHandler.getAllWorn(player).map(inv -> {
			for (int i = 0; i < inv.getSlots(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (!stack.isEmpty()) {
					Item item = stack.getItem();
					if (item instanceof IBurstViewerBauble) {
						return true;
					}

					if (item instanceof ICosmeticAttachable) {
						ICosmeticAttachable attach = (ICosmeticAttachable) item;
						ItemStack cosmetic = attach.getCosmeticItem(stack);
						if (!cosmetic.isEmpty() && cosmetic.getItem() instanceof IBurstViewerBauble) {
							return true;
						}
					}
				}
			}
			return false;
		}).orElse(false);
	}

}
