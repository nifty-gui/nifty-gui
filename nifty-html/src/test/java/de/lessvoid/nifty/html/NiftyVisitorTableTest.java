package de.lessvoid.nifty.html;

import de.lessvoid.nifty.builder.PanelBuilder;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class NiftyVisitorTableTest {
  private NiftyVisitor visitor;
  private NiftyBuilderFactory builderFactoryMock;

  @Before
  public void before() {
    builderFactoryMock = createMock(NiftyBuilderFactory.class);
    visitor = new NiftyVisitor(null, builderFactoryMock, null, null);
  }

  @After
  public void after() {
    verify(builderFactoryMock);
  }

  @Test
  public void tableTagRequiresBody() throws Exception {
    replay(builderFactoryMock);

    TableTag tableTag = new TableTag();
    visitor.visitTag(tableTag);
    visitor.visitEndTag(tableTag);

    executeWithError("This looks like HTML with a missing <body> tag");
  }

  @Test
  public void tableRowTagRequiresTableTag() throws Exception {
    replay(builderFactoryMock);

    TableRow tableRow = new TableRow();
    visitor.visitTag(tableRow);
    visitor.visitEndTag(tableRow);

    executeWithError(
        "This looks like HTML with a missing <body> tag",
        "This looks like a <tr> element with a missing <table> tag");
  }

  @Test
  public void tableDataTagRequiresTableRowTag() throws Exception {
    replay(builderFactoryMock);

    TableColumn tableColumn = new TableColumn();
    visitor.visitTag(tableColumn);
    visitor.visitEndTag(tableColumn);

    executeWithError(
        "This looks like HTML with a missing <body> tag",
        "This looks like a <td> element with a missing <tr> tag");
  }

  @Test
  public void simpleBodyWithBasicImageSuccess() throws Exception {
    PanelBuilder bodyPanelBuilder = new PanelBuilder();
    PanelBuilder tablePanelBuilder = new PanelBuilder();

    expect(builderFactoryMock.createBodyPanelBuilder()).andReturn(bodyPanelBuilder);
    expect(builderFactoryMock.createTableTagPanelBuilder(null, null, null, null)).andReturn(tablePanelBuilder);
    replay(builderFactoryMock);

    BodyTag bodyTag = new BodyTag();
    visitor.visitTag(bodyTag);

      // add table
      TableTag tableTag = new TableTag();
      visitor.visitTag(tableTag);
      visitor.visitEndTag(tableTag);

    // close body
    visitor.visitEndTag(bodyTag);

    assertEquals(bodyPanelBuilder, visitor.builder());
    assertEquals(1, bodyPanelBuilder.getElementBuilders().size());
    assertEquals(tablePanelBuilder, bodyPanelBuilder.getElementBuilders().get(0));
  }

  @Test
  public void tableTagWithAttributes() throws Exception {
    PanelBuilder bodyPanelBuilder = new PanelBuilder();
    expect(builderFactoryMock.createBodyPanelBuilder()).andReturn(bodyPanelBuilder);

    PanelBuilder tablePanelBuilder = new PanelBuilder();
    expect(builderFactoryMock.createTableTagPanelBuilder("20", "#ff0000", "2", "#000000")).andReturn(tablePanelBuilder);
    replay(builderFactoryMock);

    BodyTag bodyTag = new BodyTag();
    visitor.visitTag(bodyTag);

      // add table
      TableTag tableTag = new TableTag();
      tableTag.setAttribute("width", "20");
      tableTag.setAttribute("bgcolor", "#ff0000");
      tableTag.setAttribute("border", "2");
      tableTag.setAttribute("bordercolor", "#000000");
      visitor.visitTag(tableTag);
      visitor.visitEndTag(tableTag);

    // close body
    visitor.visitEndTag(bodyTag);

    assertEquals(bodyPanelBuilder, visitor.builder());
    assertEquals(1, bodyPanelBuilder.getElementBuilders().size());
    assertEquals(tablePanelBuilder, bodyPanelBuilder.getElementBuilders().get(0));
  }

  private void executeWithError(final String ... message) {
    StringBuffer expectedError = new StringBuffer();
    for (String m : message) {
      expectedError.append(m);
      expectedError.append("\n");
    }
    try {
      visitor.builder();
    } catch (Exception e) {
      assertEquals(expectedError.toString(), e.getMessage());
    }
  }
}
