package de.lessvoid.nifty.java2d.tests;

import java.awt.Font;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.java2d.renderer.FontProviderJava2dImpl;
import de.lessvoid.nifty.loaderv2.NiftyLoader;
import de.lessvoid.nifty.loaderv2.types.NiftyType;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;

public class GameExampleApp extends NiftyJava2dWindow {


	public static class Screens {

		public static final String INTRODUCTION_SCREEN = "introductionScreen";
		
		public static final String MAIN_MENU_SCREEN = "mainMenuScreen";
		
		public static final String CREDITS_SCREEN = "creditsScreen";
		
	}

	public static class IntroductionScreenController implements
			ScreenController {

		private Nifty nifty;

		@Override
		public void onStartScreen() {
			nifty.gotoScreen(Screens.MAIN_MENU_SCREEN);
		}

		@Override
		public void onEndScreen() {
			System.out.println("introduction screen ended");
		}

		@Override
		public void bind(Nifty nifty, Screen screen) {
			this.nifty = nifty;
		}

	}

	public static class CreditsScreenController implements ScreenController {

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
		
		public void back() {
			nifty.gotoScreen(Screens.MAIN_MENU_SCREEN);
		}

	}

	public static class MainMenuController implements ScreenController {
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

		public void play() {

		}

		public void options() {

		}

		public void highscores() {

		}

		public void credits() {
			nifty.gotoScreen(Screens.CREDITS_SCREEN);
		}

		public void exit() {
			nifty.exit();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new GameExampleApp(
				"Nifty Java2d Renderer - HelloWolrd example with builder", 800,
				600).start();
	}

	public GameExampleApp(String title, int width, int height) {
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
		 * 
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
		 * 
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

	public static class FadeEffectBuilder extends EffectBuilder {

		public FadeEffectBuilder() {
			super("fade");
		}

		public void startColor(String startColor) {
			attributes.setAttribute("startColor", startColor);
		}

		public void endColor(String endColor) {
			attributes.setAttribute("endColor", endColor);
		}

	}

	public static String convertColor(Color color) {
		return color.toString();
	}

	@Override
	protected void init() {

		NiftyType niftyType = new NiftyType();

		try {
			NiftyLoader niftyLoader = nifty.getLoader();
			niftyLoader.loadStyleFile("nifty-styles.nxs",
					"nifty-default-styles.xml", niftyType);
			niftyLoader.loadControlFile("nifty-controls.nxs",
					"nifty-default-controls.xml", niftyType);
			niftyType.create(nifty, nifty.getTimeProvider());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Screen introductionScreen = new ScreenBuilder(Screens.INTRODUCTION_SCREEN) {
			{
				controller(new IntroductionScreenController());

				inputMapping("de.lessvoid.nifty.input.mapping.DefaultScreenMapping");

				layer(new LayerBuilder("background") {
					{

						backgroundImage("intro-background.png");

						onStartScreenEffect(new FadeEffectBuilder() {
							{

								startColor("#fff0");
								endColor("#ffff");
								length(500);
								startDelay(1000);
								inherit(true);
								post(false);

							}
						});

					}
				});

				layer(new LayerBuilder("logoLayer") {
					{
						childLayoutCenter();

						onStartScreenEffect(new FadeEffectBuilder() {
							{

								startColor("#0000");
								endColor("#000f");
								length(300);
								startDelay(1500);
								inherit(true);
								post(false);

							}
						});

						onEndScreenEffect(new FadeEffectBuilder() {
							{
								startColor("#000f");
								endColor("#0000");
								length(500);
								startDelay(1500);
								inherit(true);
								post(false);

							}
						});

						panel(new PanelBuilder("panel") {
							{

								childLayoutCenter();
								// backgroundImage("intro-background.png");
								width(percentage(100));

								onStartScreenEffect(new FadeEffectBuilder() {
									{

										startColor("#fff0");
										endColor("#ffff");

										length(1000);
										post(false);

									}
								});

								panel(new PanelBuilder() {
									{
										childLayoutVertical();

										valign(VAlign.Center);
										align(Align.Center);

										onEndScreenEffect(new FadeEffectBuilder() {
											{

												startColor("#ffff");
												endColor("#0000");
												length(200);
												startDelay(1000);
												inherit(true);
												post(false);

											}
										});

										image(new ImageBuilder() {
											{

												valign(VAlign.Center);
												align(Align.Center);

												filename("logo.png");

												onStartScreenEffect(new FadeEffectBuilder() {
													{

														startColor("#0000");
														endColor("#000f");
														length(2000);
														startDelay(1500);
														post(false);

													}
												});

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

		Screen mainMenuScreen = new ScreenBuilder(Screens.MAIN_MENU_SCREEN) {
			{
				controller(new MainMenuController());

				layer(new LayerBuilder("background") {
					{

						backgroundImage("intro-background.png");

					}
				});

				layer(new LayerBuilder("content") {
					{

						backgroundColor("#fff0");

						onStartScreenEffect(new FadeEffectBuilder() {
							{
								startColor("#fff0");
								endColor("#ffff");
								length(1000);
								startDelay(0);
								inherit(true);
								post(false);

							}
						});

						childLayoutVertical();

						panel(new PanelBuilder("top") {
							{
								// style("screen-top");
								backgroundColor("#f006");
								height(pixels(100));
							}
						});

						panel(new PanelBuilder("middle") {
							{
								// style("screen-middle");
								backgroundColor("#0f06");

								childLayoutCenter();

								width(percentage(80));
								height(percentage(40));

								alignCenter();
								valignCenter();

								height("*");

								// backgroundColor("#f603");

								visibleToMouse();
								padding(pixels(10));

								onStartScreenEffect(new MoveEffectBuilder() {
									{
										mode(inMode());
										direction(leftDirection());
										length(500);
										startDelay(0);
										inherit(true);
									}
								});
								
								onEndScreenEffect(new MoveEffectBuilder() {
									{
										mode(outMode());
										direction(rightDirection());
										length(500);
										startDelay(0);
										inherit(true);
									}
								});

								panel(new PanelBuilder("menu-main") {
									{

										childLayoutVertical();
										alignCenter();
										valignCenter();
										width(percentage(100));

										control(new ControlBuilder(
												"playButton", "button") {
											{

												set("label", "Play");
												width(pixels(100));

												alignCenter();
												valignCenter();

												interactOnClick("play()");

											}
										});

										control(new ControlBuilder(
												"optionsButton", "button") {
											{

												set("label", "Options");
												width(pixels(100));

												alignCenter();
												valignCenter();

												interactOnClick("options()");

											}
										});

										control(new ControlBuilder(
												"highscoresButton", "button") {
											{

												set("label", "Highscores");
												width(pixels(100));

												alignCenter();
												valignCenter();

												interactOnClick("highscores()");

											}
										});

										control(new ControlBuilder(
												"creditsButton", "button") {
											{

												set("label", "Credits");
												width(pixels(100));

												alignCenter();
												valignCenter();

												interactOnClick("credits()");

											}
										});

										control(new ControlBuilder(
												"exitButton", "button") {
											{

												set("label", "Exit");
												width(pixels(100));

												alignCenter();
												valignCenter();

												interactOnClick("exit()");

											}
										});

									}
								});

							}
						});

						panel(new PanelBuilder("bottom") {
							{
								// style("screen-middle");
								backgroundColor("#00f6");
								height(pixels(100));
							}
						});

					}
				});
			}
		}.build(nifty);

		Screen creditsScreen = new ScreenBuilder(Screens.CREDITS_SCREEN) {
			{
				controller(new CreditsScreenController());

				inputMapping("de.lessvoid.nifty.input.mapping.DefaultScreenMapping");

				layer(new LayerBuilder("background") {
					{

						backgroundImage("intro-background.png");

					}
				});

				layer(new LayerBuilder("content") {
					{

						backgroundColor("#fff0");
						childLayoutVertical();

						onStartScreenEffect(new FadeEffectBuilder() {
							{
								startColor("#fff0");
								endColor("#ffff");
								length(1000);
								startDelay(0);
								inherit(true);
								post(false);

							}
						});

						onEndScreenEffect(new FadeEffectBuilder() {
							{
								startColor("#ffff");
								endColor("#0000");
								length(500);
								startDelay(0);
								inherit(true);
								post(false);

							}
						});
						
						interactOnClick("back()");

						panel(new PanelBuilder("top") {
							{
								backgroundColor("#f006");

								height(pixels(100));
								childLayoutCenter();
								valignCenter();
								alignCenter();

								text(new TextBuilder() {
									{
										text("Credits");
										font("aurulent-sans-17.fnt");
										color("#000f");
										width("*");
										alignCenter();
										valignCenter();

										padding(pixels(50));
									}
								});

							}
						});

						panel(new PanelBuilder("middle") {
							{
								backgroundColor("#0f06");
								
								childLayoutCenter();
								width(percentage(80));
								height("*");

								alignCenter();
								valignCenter();

								text(new TextBuilder() {
									{
										text("Lead Designer..........................................Someone\n"
												+ "Multitask Developer..........................................Anotherone\n");
										font("aurulent-sans-17.fnt");
										color("#000f");
										width("*");
										alignCenter();
										valignCenter();

										padding(pixels(50));
									}
								});

							}
						});

						panel(new PanelBuilder("bottom") {
							{
								// style("screen-middle");
								backgroundColor("#00f6");
								height(pixels(100));
							}
						});

					}
				});

			}
		}.build(nifty);

		nifty.addScreen(introductionScreen.getScreenId(), introductionScreen);
		nifty.addScreen(mainMenuScreen.getScreenId(), mainMenuScreen);
		nifty.addScreen(creditsScreen.getScreenId(), creditsScreen);

		nifty.gotoScreen(introductionScreen.getScreenId());

	}

}
