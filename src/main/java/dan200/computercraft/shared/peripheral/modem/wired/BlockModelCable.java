package dan200.computercraft.shared.peripheral.modem.wired;

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

    private static void setBounds(AABB bounds, AABB shape) {
        bounds.set(shape.minX, shape.minY, shape.minZ, shape.maxX, shape.maxY, shape.maxZ);
    }


    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int data) {
        return currentCoordinate;
    }
}
