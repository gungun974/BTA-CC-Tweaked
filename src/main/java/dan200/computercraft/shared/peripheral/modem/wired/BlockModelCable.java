package dan200.computercraft.shared.peripheral.modem.wired;

import dan200.computercraft.shared.peripheral.modem.ModemShapes;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class BlockModelCable<T extends BlockLogic> extends BlockModelStandard<T> {
    public static final IconCoordinate CABLE_CORE = TextureRegistry.getTexture("computercraft:block/cable_core");
    public static final IconCoordinate CABLE_SIDE = TextureRegistry.getTexture("computercraft:block/cable_side");

    public static final IconCoordinate MODEM_BACK = TextureRegistry.getTexture("computercraft:block/modem_back");

    public static final IconCoordinate WIRED_MODEM_FACE = TextureRegistry.getTexture("computercraft:block/wired_modem_face");
    public static final IconCoordinate WIRED_MODEM_FACE_ON = TextureRegistry.getTexture("computercraft:block/wired_modem_face_on");

    public static final IconCoordinate WIRED_MODEM_FACE_PERIPHERAL = TextureRegistry.getTexture("computercraft:block/wired_modem_face_peripheral");
    public static final IconCoordinate WIRED_MODEM_FACE_PERIPHERAL_ON = TextureRegistry.getTexture("computercraft:block/wired_modem_face_peripheral_on");


    public BlockModelCable(Block<T> block) {
        super(block);
    }

    IconCoordinate currentCoordinate = CABLE_SIDE;

    public boolean render(Tessellator tessellator, int x, int y, int z) {
        AABB bounds = this.block.getBlockBoundsFromState(renderBlocks.blockAccess, x, y, z);

        renderBlocks.enableAO = true;
        renderBlocks.cache.setupCache(this.block, renderBlocks.blockAccess, x, y, z);

        TileEntity tileEntity = renderBlocks.blockAccess.getTileEntity(x, y, z);

        if (tileEntity instanceof TileCable) {
            final TileCable state = (TileCable) tileEntity;

            if (state.blockStateCable) {
                setBounds(bounds, CableShapes.SHAPE_CABLE_CORE);
                if (!state.blockStateNorth) {
                    if (state.blockStateSouth && !state.blockStateWest && !state.blockStateEast && !state.blockStateUp && !state.blockStateDown) {
                        currentCoordinate = CABLE_CORE;
                    }
                    this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                    currentCoordinate = CABLE_SIDE;
                }
                if (!state.blockStateSouth) {
                    if (state.blockStateNorth && !state.blockStateWest && !state.blockStateEast && !state.blockStateUp && !state.blockStateDown) {
                        currentCoordinate = CABLE_CORE;
                    }
                    this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                    currentCoordinate = CABLE_SIDE;
                }
                if (!state.blockStateEast) {
                    if (state.blockStateWest && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateUp && !state.blockStateDown) {
                        currentCoordinate = CABLE_CORE;
                    }
                    if (!state.blockStateWest && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateUp && !state.blockStateDown) {
                        currentCoordinate = CABLE_CORE;
                    }

                    this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                    currentCoordinate = CABLE_SIDE;
                }
                if (!state.blockStateWest) {
                    if (state.blockStateEast && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateUp && !state.blockStateDown) {
                        currentCoordinate = CABLE_CORE;
                    }
                    if (!state.blockStateEast && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateUp && !state.blockStateDown) {
                        currentCoordinate = CABLE_CORE;
                    }
                    this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                    currentCoordinate = CABLE_SIDE;
                }
                if (!state.blockStateUp) {
                    if (state.blockStateDown && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateWest && !state.blockStateEast) {
                        currentCoordinate = CABLE_CORE;
                    }
                    this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                    currentCoordinate = CABLE_SIDE;
                }
                if (!state.blockStateDown) {
                    if (state.blockStateUp && !state.blockStateSouth && !state.blockStateNorth && !state.blockStateWest && !state.blockStateEast) {
                        currentCoordinate = CABLE_CORE;
                    }
                    this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                    currentCoordinate = CABLE_SIDE;
                }

                if (state.blockStateNorth) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.NORTH));
                    this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                }
                if (state.blockStateSouth) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.SOUTH));
                    this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                }
                if (state.blockStateEast) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.EAST));
                    this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                }
                if (state.blockStateWest) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.WEST));
                    this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                }
                if (state.blockStateUp) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.UP));
                    this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                }
                if (state.blockStateDown) {
                    setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.DOWN));
                    this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                    this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                }
            }

            if (state.blockStateModem.getFacing() != null) {
                setBounds(bounds, CableShapes.getModemShape(state));

                switch (state.blockStateModem.getFacing()) {
                    case NORTH:
                        if (state.blockStateModem == CableModemVariant.NorthOn) {
                            currentCoordinate = WIRED_MODEM_FACE_ON;
                        } else if (state.blockStateModem == CableModemVariant.NorthOffPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL;
                        } else if (state.blockStateModem == CableModemVariant.NorthOnPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL_ON;
                        } else {
                            currentCoordinate = WIRED_MODEM_FACE;
                        }

                        this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                        renderBlocks.flipTexture = true;
                        this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                        renderBlocks.flipTexture = false;
                        this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);

                        currentCoordinate = MODEM_BACK;

                        this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                        break;
                    case EAST:
                        if (state.blockStateModem == CableModemVariant.EastOn) {
                            currentCoordinate = WIRED_MODEM_FACE_ON;
                        } else if (state.blockStateModem == CableModemVariant.EastOffPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL;
                        } else if (state.blockStateModem == CableModemVariant.EastOnPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL_ON;
                        } else {
                            currentCoordinate = WIRED_MODEM_FACE;
                        }

                        renderBlocks.uvRotateTop = 1;
                        this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                        renderBlocks.flipTexture = true;
                        this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                        renderBlocks.flipTexture = false;

                        currentCoordinate = MODEM_BACK;

                        this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                        break;
                    case SOUTH:
                        if (state.blockStateModem == CableModemVariant.SouthOn) {
                            currentCoordinate = WIRED_MODEM_FACE_ON;
                        } else if (state.blockStateModem == CableModemVariant.SouthOffPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL;
                        } else if (state.blockStateModem == CableModemVariant.SouthOnPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL_ON;
                        } else {
                            currentCoordinate = WIRED_MODEM_FACE;
                        }

                        renderBlocks.uvRotateTop = 3;
                        this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                        renderBlocks.flipTexture = true;
                        this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                        renderBlocks.flipTexture = false;
                        this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);

                        currentCoordinate = MODEM_BACK;

                        this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                        break;
                    case WEST:
                        if (state.blockStateModem == CableModemVariant.WestOn) {
                            currentCoordinate = WIRED_MODEM_FACE_ON;
                        } else if (state.blockStateModem == CableModemVariant.WestOffPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL;
                        } else if (state.blockStateModem == CableModemVariant.WestOnPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL_ON;
                        } else {
                            currentCoordinate = WIRED_MODEM_FACE;
                        }

                        renderBlocks.uvRotateTop = 2;
                        this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                        renderBlocks.flipTexture = true;
                        this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);
                        renderBlocks.flipTexture = false;

                        currentCoordinate = MODEM_BACK;

                        this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                        break;
                    case UP:
                        if (state.blockStateModem == CableModemVariant.UpOn) {
                            currentCoordinate = WIRED_MODEM_FACE_ON;
                        } else if (state.blockStateModem == CableModemVariant.UpOffPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL;
                        } else if (state.blockStateModem == CableModemVariant.UpOnPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL_ON;
                        } else {
                            currentCoordinate = WIRED_MODEM_FACE;
                        }

                        this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);

                        currentCoordinate = MODEM_BACK;

                        this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                        break;
                    case DOWN:
                        if (state.blockStateModem == CableModemVariant.DownOn) {
                            currentCoordinate = WIRED_MODEM_FACE_ON;
                        } else if (state.blockStateModem == CableModemVariant.DownOffPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL;
                        } else if (state.blockStateModem == CableModemVariant.DownOnPeripheral) {
                            currentCoordinate = WIRED_MODEM_FACE_PERIPHERAL_ON;
                        } else {
                            currentCoordinate = WIRED_MODEM_FACE;
                        }

                        this.renderSide(tessellator, bounds, x, y, z, Side.SOUTH, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.TOP, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.WEST, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.EAST, 0);
                        this.renderSide(tessellator, bounds, x, y, z, Side.NORTH, 0);

                        currentCoordinate = MODEM_BACK;

                        this.renderSide(tessellator, bounds, x, y, z, Side.BOTTOM, 0);
                        break;
                    case NONE:
                }
            }

            currentCoordinate = CABLE_SIDE;
        }

        this.resetRenderBlocks();
        return true;
    }

    @Override
    public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
        if (renderBlocks.useInventoryTint) {
            int color = ((BlockColor) BlockColorDispatcher.getInstance().getDispatch(this.block)).getFallbackColor(metadata);
            float r = (float)(color >> 16 & 255) / 255.0F;
            float g = (float)(color >> 8 & 255) / 255.0F;
            float b = (float)(color & 255) / 255.0F;
            GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
        } else {
            GL11.glColor4f(brightness, brightness, brightness, alpha);
        }

        float yOffset = 0.5F;
        AABB bounds = this.getBlockBoundsForItemRender();
        GL11.glTranslatef(-0.5F, 0.0F - yOffset, -0.5F);

        if (LightmapHelper.isLightmapEnabled() && lightmapCoordinate != null) {
            LightmapHelper.setLightmapCoord(lightmapCoordinate);
        }

        tessellator.startDrawingQuads();

        if (metadata == 1) {

            GL11.glScalef(1.20f, 1.20f, 1.20f);
            GL11.glTranslatef(0.4f, -0.05f, 0);

            setBounds(bounds, ModemShapes.getBounds(Direction.WEST));

            renderBlocks.uvRotateTop = 1;
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            this.renderSouthFace(tessellator, bounds, 0.0, 0.0, 0.0, WIRED_MODEM_FACE);
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            this.renderTopFace(tessellator, bounds, 0.0, 0.0, 0.0, WIRED_MODEM_FACE);
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            this.renderBottomFace(tessellator, bounds, 0.0, 0.0, 0.0, WIRED_MODEM_FACE);
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            this.renderEastFace(tessellator, bounds, 0.0, 0.0, 0.0, WIRED_MODEM_FACE);
            renderBlocks.flipTexture = true;
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            this.renderNorthFace(tessellator, bounds, 0.0, 0.0, 0.0, WIRED_MODEM_FACE);
            renderBlocks.flipTexture = false;

            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            this.renderWestFace(tessellator, bounds, 0.0, 0.0, 0.0, MODEM_BACK);
        } else {

            setBounds(bounds, CableShapes.SHAPE_CABLE_CORE);
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            this.renderNorthFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            this.renderSouthFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            this.renderTopFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            this.renderBottomFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);

            setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.EAST));

            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            this.renderNorthFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            this.renderSouthFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            this.renderTopFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            this.renderBottomFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            this.renderEastFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_CORE);

            setBounds(bounds, CableShapes.SHAPE_CABLE_ARM.get(Direction.WEST));
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            this.renderNorthFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            this.renderSouthFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            this.renderTopFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            this.renderBottomFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_SIDE);
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            this.renderWestFace(tessellator, bounds, 0.0, 0.0, 0.0, CABLE_CORE);

        }

        tessellator.draw();

        GL11.glTranslatef(0.5F, yOffset, 0.5F);
    }

    private static void setBounds(AABB bounds, AABB shape) {
        bounds.set(shape.minX, shape.minY, shape.minZ, shape.maxX, shape.maxY, shape.maxZ);
    }


    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        return currentCoordinate;
    }
}
