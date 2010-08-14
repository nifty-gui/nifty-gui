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
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.java2d.renderer.FontProviderJava2dImpl;
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

	public static final class LayoutType {
		public static String Vertical = "vertical";
		public static String Horizontal = "horizontal";
		public static String Center = "center";
		public static String Absolute = "absolute";
		public static String Overlay = "overlay";
	}

	abstract class ElementBuilder {

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

		public void align(String align) {
			elementAttributes.put("align", align);
		}

		public void valign(String valign) {
			elementAttributes.put("valign", valign);
		}

		public void visibleToMouse(String visibleToMouse) {
			elementAttributes.put("visibleToMouse", visibleToMouse);
		}

		public void interactOnClick(String method) {

		}

		public abstract Element build();

		// / temporal

		protected ControlEffectAttributes onStartScreenEffect = null;

		protected ControlEffectOnHoverAttributes onHoverEffect = null;

		public void onStartScreenEffect(
				ControlEffectAttributes onStartScreenEffect) {
			this.onStartScreenEffect = onStartScreenEffect;
		}

		public void onHoverEffect(ControlEffectOnHoverAttributes onHoverEffect) {
			this.onHoverEffect = onHoverEffect;

		}
	}

	class PanelBuilder extends ElementBuilder {

		@Override
		public Element build() {

			if (id == null)
				throw new RuntimeException("id is a required value for a panel");

			if (screen == null)
				throw new RuntimeException(
						"screen is a required value for a panel");

			if (parent == null)
				throw new RuntimeException(
						"parent is a required value for a panel");

			return new PanelCreator(id) {
				{
					for (String name : elementAttributes.keySet())
						set(name, elementAttributes.get(name));

					if (onStartScreenEffect != null)
						addEffectsOnStartScreen(onStartScreenEffect);

					if (onHoverEffect != null)
						addEffectsOnHover(onHoverEffect);
				}
			}.create(nifty, screen, parent);
		}

	}

	class LayerBuilder extends ElementBuilder {

		private List<ElementBuilder> elementBuilders = new ArrayList<ElementBuilder>();

		public Element build() {

			if (id == null)
				throw new RuntimeException("id is a required value for a layer");

			if (screen == null)
				throw new RuntimeException(
						"screen is a required value for a layer");

			if (parent == null)
				throw new RuntimeException(
						"parent is a required value for a layer");

			Element layer = new LayerCreator(id) {
				{
					for (String name : elementAttributes.keySet())
						set(name, elementAttributes.get(name));
				}
			}.create(nifty, screen, parent);

			for (ElementBuilder elementBuilder : elementBuilders) {

				elementBuilder.parent(layer);
				elementBuilder.screen(screen);

				Element childElement = elementBuilder.build();

				layer.add(childElement);
			}

			return layer;
		}

		public void panel(PanelBuilder panelBuilder) {
			elementBuilders.add(panelBuilder);
		}

	}

	class ScreenBuilder {

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

			if (id == null)
				throw new RuntimeException(
						"id is a required value for a screen");

			if (controller == null)
				throw new RuntimeException(
						"controller is a required value for a screen");

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
				layerBuilder.build();

				// screen.addLayerElement(layer);
				// screen.processAddAndRemoveLayerElements();
			}

			return screen;
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

						backgroundColor("#003f");
						childLayout(LayoutType.Center);

						panel(new PanelBuilder() {
							{
								id("panel");

								height("25%");
								width("80%");
								align("center");
								valign("center");
								backgroundColor("#f60f");
								childLayout(LayoutType.Center);

								visibleToMouse("true");

								interactOnClick("quit()"); // unimplemented!!
								// could be directly
								// a callable?

								// effects {
								// onStartScreen {
								//										
								// }
								// }

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

								onHoverEffect(new ControlEffectOnHoverAttributes() {
									{
										setAttribute("name", "pulsate");
										setAttribute("scaleFactor", "0.008");
										setAttribute("startColor", "#f600");
										setAttribute("endColor", "#ffff");
										setAttribute("post", "true");
									}
								});
								
							}
						});
					}
				});

			}
		}.build();

		// Screen screen = new Screen(nifty, "start",
		// new HelloWorldMainScreenController(), nifty.getTimeProvider());

		List<Element> layerElements = screen.getLayerElements();
		Element layer = layerElements.get(0);

		Element panel = layer.getElements().get(0);

		// Element rootLayer = NiftyFactory.createRootLayer("layer", nifty,
		// screen, nifty.getTimeProvider());
		//
		// Element layer = new LayerCreator("layer") {
		// {
		// setBackgroundColor("#003f");
		// setChildLayout("center");
		// }
		// }.create(nifty, screen, rootLayer);

		// Element panel = new PanelCreator("panel") {
		// {
		// setHeight("25%");
		// setWidth("80%");
		// setAlign("center");
		// setVAlign("center");
		// setBackgroundColor("#f60f");
		// setChildLayout("center");
		// setVisibleToMouse("true");
		// setInteractOnClick("quit()");
		//
		// addEffectsOnStartScreen(new ControlEffectAttributes() {
		// {
		// setAttribute("name", "move");
		// setAttribute("mode", "in");
		// setAttribute("direction", "top");
		// setAttribute("length", "300");
		// setAttribute("startDelay", "0");
		// setAttribute("inherit", "true");
		// }
		// });
		//
		// addEffectsOnEndScreen(new ControlEffectAttributes() {
		// {
		// setAttribute("name", "move");
		// setAttribute("mode", "out");
		// setAttribute("direction", "bottom");
		// setAttribute("length", "300");
		// setAttribute("startDelay", "0");
		// setAttribute("inherit", "true");
		// }
		// });
		//
		// addEffectsOnHover(new ControlEffectOnHoverAttributes() {
		// {
		// setAttribute("name", "pulsate");
		// setAttribute("scaleFactor", "0.008");
		// setAttribute("startColor", "#f600");
		// setAttribute("endColor", "#ffff");
		// setAttribute("post", "true");
		// }
		// });
		//
		// }
		// }.create(nifty, screen, layer);

		panel.add(new TextCreator("text", "Hello World!") {
			{
				setFont("arial.fnt");
				setColor("#000f");
				setAlign("center");
				setVAlign("center");
			}
		}.create(nifty, screen, panel));

		layer.add(panel);

		screen.addLayerElement(layer);

		nifty.addScreen("start", screen);
		nifty.gotoScreen("start");

		// nifty.fromXml("all/intro.xml", "start");
	}

}
