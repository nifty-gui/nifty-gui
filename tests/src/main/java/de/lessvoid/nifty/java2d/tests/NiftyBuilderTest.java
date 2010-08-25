package de.lessvoid.nifty.java2d.tests;

import java.awt.Font;

import de.lessvoid.nifty.NiftyDefaults;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.java2d.renderer.FontProviderJava2dImpl;
import de.lessvoid.nifty.java2d.tests.controllers.HelloWorldMainScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.spi.sound.SoundDeviceNullImpl;

public class NiftyBuilderTest extends NiftyJava2dWindow {

	public static void main(String[] args) throws InterruptedException {
		new NiftyBuilderTest("Nifty Java2d Renderer - HelloWolrd example",
				1024, 768).start();
	}

	public NiftyBuilderTest(String title, int width, int height) {
		super(title, width, height, new SoundDeviceNullImpl());
	}

	@Override
	protected void registerFonts(FontProviderJava2dImpl fontProviderJava2dImpl) {
		fontProviderJava2dImpl.addFont("arial.fnt", new Font("arial",
				Font.BOLD, 24));
	}
	
	@Override
	protected void init() {

		NiftyDefaults.initDefaultEffects(nifty);
		
		HelloWorldMainScreenController controller = new HelloWorldMainScreenController();

		Screen screen = new ScreenBuilder("screen", controller) {
			{
				layer(new LayerBuilder() {
					{
						id("layer");
						
						// I want backgroundColor to be a Color but I dunno how
						backgroundColor("#003f");
						childLayout(ElementBuilder.LayoutType.Center);

						panel(new PanelBuilder() {
							{
								id("panel");

								height(percentage(25));
								width(percentage(80));

								align(ElementBuilder.Align.Center);
								valign(ElementBuilder.VAlign.Center);
								backgroundColor("#f60f");
								childLayout(ElementBuilder.LayoutType.Center);

								visibleToMouse("true");

								// I want this to be a callable maybe
								interactOnClick("quit()");

								onStartScreenEffect(new ControlEffectAttributes() {
									{
										setAttribute("name", "move");
										setAttribute("mode", "in");
										setAttribute("direction", "top");
										setAttribute("length", "300");
										setAttribute("startDelay", "0");
										setAttribute("inherit", "true");
									}
								});

								onEndScreenEffect(new ControlEffectAttributes() {
									{
										setAttribute("name", "move");
										setAttribute("mode", "out");
										setAttribute("direction", "bottom");
										setAttribute("length", "300");
										setAttribute("startDelay", "0");
										setAttribute("inherit", "true");
									}
								});

								onHoverEffect(new ControlEffectOnHoverAttributes() {
									{
										setAttribute("name", "pulsate");
										setAttribute("scaleFactor", "0.008");
										setAttribute("startColor", "#f600");
										setAttribute("endColor", "#ffff");
										setAttribute("post", "true");
									}
								});

								text(new TextBuilder() {
									{
										id("text1");
										text("Hello World!!");

										font("arial.fnt");
										color("#000f");
										align(ElementBuilder.Align.Center);
										valign(ElementBuilder.VAlign.Center);
									}
								});
								
							}
						});

					}
				});

			}
		}.build(nifty);

		nifty.addScreen("start", screen);
		nifty.gotoScreen("start");

		// nifty.fromXml("all/intro.xml", "start");
	}

}
