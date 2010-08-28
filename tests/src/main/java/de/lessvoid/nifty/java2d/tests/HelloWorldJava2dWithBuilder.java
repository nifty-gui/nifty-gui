package de.lessvoid.nifty.java2d.tests;

import java.awt.Font;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.HoverEffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.java2d.renderer.FontProviderJava2dImpl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class HelloWorldJava2dWithBuilder extends NiftyJava2dWindow {

	public static class ScreenControllerExample implements ScreenController {
		private Nifty nifty;

		@Override
		public void onStartScreen() {
		}

		@Override
		public void onEndScreen() {
		}

		@Override
		public void bind(Nifty nifty, Screen screen) {
			this.nifty = nifty;
		}

		public void quit() {
			nifty.exit();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new HelloWorldJava2dWithBuilder(
				"Nifty Java2d Renderer - HelloWolrd example with builder", 800,
				600).start();
	}

	public HelloWorldJava2dWithBuilder(String title, int width, int height) {
		super(title, width, height);
	}

	@Override
	protected void registerFonts(FontProviderJava2dImpl fontProviderJava2dImpl) {
		fontProviderJava2dImpl.addFont("arial.fnt", new Font("arial",
				Font.BOLD, 24));
	}
	
	public static class MoveEffectBuilder extends EffectBuilder {

		public MoveEffectBuilder() {
			super("move");
		}
		
		/**
		 * Possible values are "in" and "out"
		 * @param mode
		 */
		public void mode(String mode) {
			parameter("mode", mode);
		}
		
		public String inMode() {
			return "in";
		}

		public String outMode() {
			return "out";
		}
		
		/**
		 * Possible values are "top", "bottom", "left", "right"
		 * @param direction
		 */
		public void direction(String direction) {
			parameter("direction", direction);
		}
		
		public String topDirection() {
			return "top";
		}

		public String bottomDirection() {
			return "bottom";
		}
		
		public String leftDirection() {
			return "left";
		}
		
		public String rightDirection() {
			return "right";
		}
		
	}

	@Override
	protected void init() {

		Screen screen = new ScreenBuilder("start") {
			{
				controller(new ScreenControllerExample());

				layer(new LayerBuilder("layer") {
					{
						backgroundColor("#003f");
						childLayoutCenter();

						panel(new PanelBuilder() {
							{
								id("panel");
								childLayoutCenter();
								height("25%");
								width("80%");
								alignCenter();
								valignCenter();
								backgroundColor("#f60f");
								visibleToMouse();
								interactOnClick("quit()");
								padding("10px");

								onStartScreenEffect(new MoveEffectBuilder() {
									{
										mode(inMode());
										direction(topDirection());
										
//										parameter("mode", "in");
//										parameter("direction", "top");
										
										length(300);
										startDelay(0);
										inherit(true);
									}
								});

								onEndScreenEffect(new MoveEffectBuilder() {
									{
										mode(outMode());
										direction(bottomDirection());
										
//										parameter("mode", "out");
//										parameter("direction", "bottom");
										
										length(300);
										startDelay(0);
										inherit(true);
									}
								});

								onHoverEffect(new HoverEffectBuilder("pulsate") {
									{
										parameter("scaleFactor", "0.008");
										parameter("startColor", "#f600");
										parameter("endColor", "#ffff");
										post(true);
									}
								});

								panel(new PanelBuilder() {
									{
										childLayoutHorizontal();
										alignCenter();
										valignCenter();
										width("100%");

										image(new ImageBuilder() {
											{
												filename("nifty-logo-150x150.png");
											}
										});

										text(new TextBuilder() {
											{
												text("Hello Nifty Builder World!!!");
												font("aurulent-sans-17.fnt");
												color("#000f");
												width("*");
												alignCenter();
												valignCenter();
											}
										});
									}
								});
							}
						});
					}
				});
			}
		}.build(nifty);

		nifty.addScreen("startScreen", screen);
		nifty.gotoScreen("startScreen");
	}

}
