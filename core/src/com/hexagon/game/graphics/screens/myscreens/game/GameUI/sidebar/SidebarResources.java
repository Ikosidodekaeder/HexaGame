package com.hexagon.game.graphics.screens.myscreens.game.GameUI.sidebar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.Logic.HexaComponents;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UiElement;
import com.hexagon.game.graphics.ui.UpdateEvent;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketTradeMoney;

/**
 * Created by Sven on 01.03.2018.
 */

public class SidebarResources extends Sidebar {

    public final UILabel OreResource = new UILabel(0,0,200,0,28,"0 Ore");
    public final UILabel WoodResource = new UILabel(0,0,200,0,28,"0 Wood");
    public final UILabel StoneResource = new UILabel(0,0,200,0,28,"0 Stone");
    public final UILabel MetalResource = new UILabel(0,0,200,0,28,"0 Metal");
    public final UILabel FoodResource = new UILabel(0,0,200,0,28,"0 Food");

    private int getBuySellAmount() {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            return 10;
        }
        return 1;
    }

    public SidebarResources(GroupWindow window, Stage stage) {
        super(window, stage, 5, 5, Gdx.graphics.getHeight()/2-5);

        OreResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                OreResource.getLabel().setText(GameManager.instance.getPlayerResource(HexaComponents.ORE.name()) + " Ore");
            }
        });

        WoodResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                WoodResource.getLabel().setText(GameManager.instance.getPlayerResource(HexaComponents.WOOD.name()) + " Wood");
            }
        });

        StoneResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                StoneResource.getLabel().setText(GameManager.instance.getPlayerResource(HexaComponents.STONE.name()) + " Stone");
            }
        });

        MetalResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                MetalResource.getLabel().setText(GameManager.instance.getPlayerResource(HexaComponents.METAL.name()) + " Metal");
            }
        });

        FoodResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                int amount = GameManager.instance.getPlayerResource(HexaComponents.FOOD.name());
                FoodResource.getLabel().setText(amount + " Food");
                if (amount <= 15) {
                    FoodResource.getLabel().getStyle().fontColor = Color.RED;
                } else if (amount < 60) {
                    FoodResource.getLabel().getStyle().fontColor = Color.ORANGE;
                } else {
                    FoodResource.getLabel().getStyle().fontColor = Color.WHITE;
                }
            }
        });

        UiResourceTrade oreTrade = new UiResourceTrade(OreResource.getHeight(),HexaComponents.ORE);
        UiResourceTrade woodTrade = new UiResourceTrade(OreResource.getHeight(),HexaComponents.WOOD);
        UiResourceTrade stoneTrade = new UiResourceTrade(OreResource.getHeight(),HexaComponents.STONE);
        UiResourceTrade metalTrade = new UiResourceTrade(OreResource.getHeight(),HexaComponents.METAL);
        UiResourceTrade foodTrade = new UiResourceTrade(OreResource.getHeight(),HexaComponents.FOOD);

        oreTrade.getSell().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketTradeMoney(
                                null,
                                HexaServer.senderId,
                                HexaComponents.ORE,
                                -getBuySellAmount()
                        )
                );
            }
        });

        oreTrade.getBuy().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketTradeMoney(
                                null,
                                HexaServer.senderId,
                                HexaComponents.ORE,
                                getBuySellAmount()
                        )
                );
            }
        });

        woodTrade.getSell().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketTradeMoney(
                                null,
                                HexaServer.senderId,
                                HexaComponents.WOOD,
                                -getBuySellAmount()
                        )
                );
            }
        });

        woodTrade.getBuy().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketTradeMoney(
                                null,
                                HexaServer.senderId,
                                HexaComponents.WOOD,
                                getBuySellAmount()
                        )
                );
            }
        });

        stoneTrade.getSell().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketTradeMoney(
                                null,
                                HexaServer.senderId,
                                HexaComponents.STONE,
                                -getBuySellAmount()
                        )
                );
            }
        });

        stoneTrade.getBuy().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketTradeMoney(
                                null,
                                HexaServer.senderId,
                                HexaComponents.STONE,
                                getBuySellAmount()
                        )
                );
            }
        });

        foodTrade.getSell().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketTradeMoney(
                                null,
                                HexaServer.senderId,
                                HexaComponents.FOOD,
                                -getBuySellAmount()
                        )
                );
            }
        });

        foodTrade.getBuy().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketTradeMoney(
                                null,
                                HexaServer.senderId,
                                HexaComponents.FOOD,
                                getBuySellAmount()
                        )
                );
            }
        });

        metalTrade.getSell().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketTradeMoney(
                                null,
                                HexaServer.senderId,
                                HexaComponents.METAL,
                                -getBuySellAmount()
                        )
                );
            }
        });

        metalTrade.getBuy().addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.instance.server.send(
                        new PacketTradeMoney(
                                null,
                                HexaServer.senderId,
                                HexaComponents.METAL,
                                getBuySellAmount()
                        )
                );
            }
        });

        statusWindow.add(OreResource, stage);
        statusWindow.add(oreTrade, stage);
        statusWindow.add(WoodResource, stage);
        statusWindow.add(woodTrade, stage);
        statusWindow.add(StoneResource, stage);
        statusWindow.add(stoneTrade, stage);
        statusWindow.add(MetalResource, stage);
        statusWindow.add(metalTrade, stage);
        statusWindow.add(FoodResource, stage);
        statusWindow.add(foodTrade, stage);

        int i = 1;
        for (UiElement element : statusWindow.getElementList()) {
            element.setHeight(element.getHeight() + 10);
            element.setX(element.getX() + 5);
            i++;
        }
        statusWindow.orderAllNeatly(1);
        /*for (int i=1; i<=statusWindow.getElementList().size(); i += 2) {
            UiElement element = statusWindow.getElementList().get(i);
            UiElement lastElem = statusWindow.getElementList().get(i-1);
            element.setDisplayY(lastElem.getDisplayY() - element.getHeight());
            //ConsoleColours.Print(ConsoleColours.RED_BOLD, element.getClass().getName() + ": " + element.getY() + " /// " + element.getDisplayY());
        }*/
        //statusWindow.updateElements();


    }

    public class UiResourceTrade extends UiElement {

        private UiButton    buy;
        private UILabel     label;
        private UiButton    sell;
        private UILabel     price;
        private UILabel     available;

        private UiElement[] elements = new UiElement[5];


        public UiResourceTrade(float height,final HexaComponents type) {
            super(0, 0, 100, 0);


            buy = new UiButton("Buy 1", 0, 0, 0, 0, 28);
            buy.getTextButton().getStyle().fontColor = new Color(0.3f, 0.8f, 0.3f, 1);

            label = new UILabel(0, 0, 0, 0, 28, " - ");

            sell = new UiButton("Sell 1", 0, 0, 0, 0, 28);
            sell.getTextButton().getStyle().fontColor = new Color(0.85f, 0.3f, 0.3f, 1);

            price = new UILabel(0, 0, 0, 0, 28, " 50$ ");

            available = new UILabel(0,0,0,0,28,"----");
            available.getLabel().getStyle().fontColor = Color.GRAY;

            available.setUpdateEvent(new UpdateEvent() {
                @Override
                public void onUpdate() {
                    long marketPrice = GameManager.instance.getPrice(type.name());
                    if (marketPrice == -1) {
                        price.getLabel().setText(" X ");
                        price.getLabel().getStyle().fontColor = Color.RED;
                    } else {
                        price.getLabel().setText(" " + (marketPrice*getBuySellAmount()) +"$ ");
                        price.getLabel().getStyle().fontColor = Color.LIME;
                    }
                    available.getLabel().setText(" Market: " + (int) (Math.floor(GameManager.instance.getGlobalMarketResources().get(type.toString()))));
                }
            });

            buy.setUpdateEvent(new UpdateEvent() {
                @Override
                public void onUpdate() {
                    buy.getTextButton().setText("Buy " + getBuySellAmount());
                }
            });

            sell.setUpdateEvent(new UpdateEvent() {
                @Override
                public void onUpdate() {
                    sell.getTextButton().setText("Sell " + getBuySellAmount());
                }
            });

            elements[0] = buy;
            elements[1] = label;
            elements[2] = sell;
            elements[3] = price;
            elements[4] = available;


            setHeight(height);
        }

        @Override
        public void addToStage(Stage stage) {
            for (UiElement element : elements) {
                element.addToStage(stage);
            }
        }

        @Override
        public void removeFromStage(Stage stage) {
            for (UiElement element : elements) {
                element.removeFromStage(stage);
            }
        }

        @Override
        public void show(Stage stage) {
            for (UiElement element : elements) {
                element.show(stage);
            }
        }

        @Override
        public void hide(Stage stage) {
            for (UiElement element : elements) {
                element.hide(stage);
            }
        }

        @Override
        public void setX(float x) {
            super.setX(x);
            for (UiElement element : elements) {
                element.setX(x);
            }
        }

        /*@Override
        public void setY(float y) {
            super.setY(y);
            //GameManager.instance.messageUtil.add("Set Y to " + y);
            System.out.println("set y to " + y);
            for (UiElement element : elements) {
                //element.setY(y);
            }
        }*/

        @Override
        public void setHeight(float height) {
            super.setHeight(height);
            for (UiElement element : elements) {
                //element.setHeight(height);
            }
        }

        @Override
        public void setDisplayX(float displayX) {
            super.setDisplayX(displayX);
            float lastX = 10;
            for (UiElement element : elements) {
                element.setDisplayX(displayX + lastX);
                lastX += element.getWidth() + 20;
            }
        }

        @Override
        public void setDisplayY(float displayY) {
            super.setDisplayY(displayY);
            for (UiElement element : elements) {
                element.setDisplayY(displayY);
            }
        }

        @Override
        public void update() {
            super.update();
            for (UiElement element : elements) {
                element.update();
            }
        }

        public UiButton getBuy() {
            return buy;
        }

        public UILabel getLabel() {
            return label;
        }

        public UiButton getSell() {
            return sell;
        }
    }

}
