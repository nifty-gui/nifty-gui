package de.lessvoid.nifty.java2d.tests;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyDefaults;
import de.lessvoid.nifty.controls.dynamic.LayerCreator;
import de.lessvoid.nifty.controls.dynamic.PanelCreator;
import de.lessvoid.nifty.controls.dynamic.TextCreator;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectOnHoverAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlEffectsAttributes;
import de.lessvoid.nifty.controls.dynamic.attributes.ControlInteractAttributes;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.java2d.renderer.FontProviderJava2dImpl;
import de.lessvoid.nifty.java2d.tests.NiftyBuilderTest.ElementBuilder.Align;
import de.lessvoid.nifty.java2d.tests.NiftyBuilderTest.ElementBuilder.LayoutType;
import de.lessvoid.nifty.java2d.tests.NiftyBuilderTest.ElementBuilder.VAlign;
import de.lessvoid.nifty.java2d.tests.controllers.HelloWorldMainScreenController;
import de.lessvoid.nifty.loaderv2.NiftyFactory;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
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

	abstract static class ElementBuilder {

		public static final class LayoutType {
			public static String Vertical = "vertical";
			public static String Horizontal = "horizontal";
			public static String Center = "center";
			public static String Absolute = "absolute";
			public static String Overlay = "overlay";
		}

		public static final class Align {
			public static String Left = "left";
			public static String Right = "right";
			public static String Center = "center";
		}

		public static final class VAlign {
			public static String Top = "top";
			public static String Bottom = "bottom";
			public static String Center = "center";
		}

		protected Screen screen = null;

		protected String id = null;

		protected Element parent = null;

		Map<String, String> elementAttributes = new HashMap<String, String>();

		public void id(String id) {
			this.id = id;
		}

		public void screen(Screen screen) {
			this.screen = screen;
		}

		public void parent(Element parent) {
			this.parent = parent;
		}

		public void backgroundColor(String backgroundColor) {
			elementAttributes.put("backgroundColor", backgroundColor);
		}

		public void color(String color) {
			elementAttributes.put("color", color);
		}

		// I can't ....
		// public void color(Color color) {
		// elementAttributes.put("color", color.toString());
		// }

		/**
		 * @param childLayout
		 *            use LayoutType.
		 */
		public void childLayout(String childLayout) {
			elementAttributes.put("childLayout", childLayout);
		}

		public void height(String height) {
			elementAttributes.put("height", height);
		}

		public void width(String width) {
			elementAttributes.put("width", width);
		}

		/**
		 * Use Align static members
		 * 
		 * @param align
		 */
		public void align(String align) {
			elementAttributes.put("align", align);
		}

		/**
		 * Use VAlign static members
		 * 
		 * @param valign
		 */
		public void valign(String valign) {
			elementAttributes.put("valign", valign);
		}

		public void visibleToMouse(String visibleToMouse) {
			elementAttributes.put("visibleToMouse", visibleToMouse);
		}

		public void font(String font) {
			elementAttributes.put("font", font);
		}

		// children

		private List<ElementBuilder> elementBuilders = new ArrayList<ElementBuilder>();

		protected void addChildrenFor(Nifty nifty, Element element) {
			for (ElementBuilder elementBuilder : elementBuilders) {

				elementBuilder.parent(element);
				elementBuilder.screen(screen);

				Element childElement = elementBuilder.build(nifty);

				element.add(childElement);
			}
		}

		public void panel(PanelBuilder panelBuilder) {
			elementBuilders.add(panelBuilder);
		}

		public void text(TextBuilder textBuilder) {
			elementBuilders.add(textBuilder);
		}

		public abstract Element build(Nifty nifty);

		protected void validate() {
			if (id == null)
				throw new RuntimeException(
						"id is a required value for an element");

			if (screen == null)
				throw new RuntimeException(
						"screen is a required value for an element");

			if (parent == null)
				throw new RuntimeException(
						"parent is a required value for an element");
		}

		// effects...

		ControlEffectsAttributes effectsAttributes = new ControlEffectsAttributes();

		public void onStartScreenEffect(
				ControlEffectAttributes onStartScreenEffect) {
			effectsAttributes.addOnStartScreen(onStartScreenEffect);
		}

		public void onHoverEffect(ControlEffectOnHoverAttributes onHoverEffect) {
			effectsAttributes.addOnHover(onHoverEffect);
		}

		public void onEndScreenEffect(ControlEffectAttributes onEndScreenEffect) {
			effectsAttributes.addOnEndScreen(onEndScreenEffect);
		}

		// interact

		ControlInteractAttributes interactAttributes = new ControlInteractAttributes();

		public void interactOnClick(String method) {
			interactAttributes.setOnClick(method);
		}
	}

	static class TextBuilder extends ElementBuilder {

		protected String text;

		@Override
		public Element build(Nifty nifty) {

			validate();

			Element text = new TextCreator(id, this.text) {
				{
					for (String name : elementAttributes.keySet())
						set(name, elementAttributes.get(name));

					setEffects(effectsAttributes);
					setInteract(interactAttributes);

				}
			}.create(nifty, screen, parent);

			addChildrenFor(nifty, text);

			return text;
		}

		public void text(String text) {
			this.text = text;
		}

	}

	static class PanelBuilder extends ElementBuilder {

		@Override
		public Element build(Nifty nifty) {

			validate();

			Element panel = new PanelCreator(id) {
				{
					for (String name : elementAttributes.keySet())
						set(name, elementAttributes.get(name));

					setEffects(effectsAttributes);
					setInteract(interactAttributes);
				}
			}.create(nifty, screen, parent);

			addChildrenFor(nifty, panel);

			return panel;
		}

	}

	static class LayerBuilder extends ElementBuilder {

		public Element build(Nifty nifty) {

			validate();

			Element layer = new LayerCreator(id) {
				{
					for (String name : elementAttributes.keySet())
						set(name, elementAttributes.get(name));

					setEffects(effectsAttributes);
					setInteract(interactAttributes);
				}
			}.create(nifty, screen, parent);

			addChildrenFor(nifty, layer);

			return layer;
		}

	}

	static class ScreenBuilder {

		private final Nifty nifty;

		private String id = null;

		private ScreenController controller = null;

		private List<LayerBuilder> layerBuilders = new ArrayList<LayerBuilder>();

		public ScreenBuilder(Nifty nifty) {
			this.nifty = nifty;
		}

		public void id(String id) {
			this.id = id;
		}

		public void controller(ScreenController controller) {
			this.controller = controller;
		}

		public Screen build() {

			// validation

			validate();

			Screen screen = new Screen(nifty, id, controller, nifty
					.getTimeProvider());
			Element rootElement = NiftyFactory.createRootLayer("layer", nifty,
					screen, nifty.getTimeProvider());
			screen.setRootElement(rootElement);

			for (LayerBuilder layerBuilder : layerBuilders) {
				layerBuilder.screen(screen);

				if (layerBuilder.parent == null)
					layerBuilder.parent(rootElement);

				// already inserts the layer element in LAyerCreator
				layerBuilder.build(nifty);

				// screen.addLayerElement(layer);
				// screen.processAddAndRemoveLayerElements();
			}

			return screen;
		}

		protected void validate() {
			if (id == null)
				throw new RuntimeException(
						"id is a required value for a screen");

			if (controller == null)
				throw new RuntimeException(
						"controller is a required value for a screen");
		}

		public void layer(LayerBuilder layerBuilder) {
			layerBuilders.add(layerBuilder);
		}

	}

	@Override
	protected void init() {

		NiftyDefaults.initDefaultEffects(nifty);

		Screen screen = new ScreenBuilder(nifty) {
			{
				id("screen");

				controller(new HelloWorldMainScreenController());

				layer(new LayerBuilder() {
					{
						id("layer");

						// I want backgroundColor to be a Color but I dunno how
						backgroundColor("#003f");
						childLayout(LayoutType.Center);

						panel(new PanelBuilder() {
							{
								id("panel");

								height("25%");
								width("80%");
								align(Align.Center);
								valign(VAlign.Center);
								backgroundColor("#f60f");
								childLayout(LayoutType.Center);

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
										align(Align.Center);
										valign(VAlign.Center);
									}
								});

							}
						});
					}
				});

			}
		}.build();

		nifty.addScreen("start", screen);
		nifty.gotoScreen("start");

		// nifty.fromXml("all/intro.xml", "start");
	}

}
