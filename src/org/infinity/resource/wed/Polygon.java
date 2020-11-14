// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2020 Jon Olav Hauglid
// See LICENSE.txt for license information

package org.infinity.resource.wed;

import java.nio.ByteBuffer;

import org.infinity.datatype.DecNumber;
import org.infinity.datatype.Flag;
import org.infinity.datatype.HexNumber;
import org.infinity.datatype.SectionCount;
import org.infinity.datatype.Unknown;
import org.infinity.resource.AbstractStruct;
import org.infinity.resource.AddRemovable;
import org.infinity.resource.HasAddRemovable;
import org.infinity.resource.StructEntry;
import org.infinity.resource.vertex.Vertex;

public abstract class Polygon extends AbstractStruct implements AddRemovable, HasAddRemovable
{
  // WED/Polygon-specific field labels
  public static final String WED_POLY_VERTEX_INDEX  = "Vertex index";
  public static final String WED_POLY_NUM_VERTICES  = "# vertices";
  public static final String WED_POLY_FLAGS         = "Polygon flags";
  public static final String WED_POLY_MIN_COORD_X   = "Minimum coordinate: X";
  public static final String WED_POLY_MAX_COORD_X   = "Maximum coordinate: X";
  public static final String WED_POLY_MIN_COORD_Y   = "Minimum coordinate: Y";
  public static final String WED_POLY_MAX_COORD_Y   = "Maximum coordinate: Y";

  public static final String[] s_flags = { "No flags set", "Shade wall", "Semi transparent",
                                            "Hovering wall", "Cover animations", null,
                                            null, null, "Is door" };

  public Polygon(AbstractStruct superStruct, String name, ByteBuffer buffer, int offset) throws Exception
  {
    super(superStruct, name, buffer, offset, 8);
  }

  //<editor-fold defaultstate="collapsed" desc="HasAddRemovable">
  @Override
  public AddRemovable[] getPrototypes() throws Exception
  {
    return new AddRemovable[]{new Vertex()};
  }

  @Override
  public AddRemovable confirmAddEntry(AddRemovable entry) throws Exception
  {
    return entry;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="AddRemovable">
  @Override
  public boolean canRemove()
  {
    return true;
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="AbstractStruct">
  @Override
  protected void setAddRemovableOffset(AddRemovable datatype)
  {
    if (datatype instanceof Vertex) {
      int index = ((DecNumber)getAttribute(WED_POLY_VERTEX_INDEX)).getValue();
      index += ((DecNumber)getAttribute(WED_POLY_NUM_VERTICES)).getValue();
      AbstractStruct superStruct = getParent();
      while (superStruct.getParent() != null)
        superStruct = superStruct.getParent();
      int offset = ((HexNumber)superStruct.getAttribute(WedResource.WED_OFFSET_VERTICES)).getValue();
      datatype.setOffset(offset + 4 * index);
      ((AbstractStruct)datatype).realignStructOffsets();
    }
  }
  //</editor-fold>

  public void readVertices(ByteBuffer buffer, int offset) throws Exception
  {
    DecNumber firstVertex = (DecNumber)getAttribute(WED_POLY_VERTEX_INDEX);
    DecNumber numVertices = (DecNumber)getAttribute(WED_POLY_NUM_VERTICES);
    for (int i = 0; i < numVertices.getValue(); i++) {
      addField(new Vertex(this, buffer, offset + 4 * (firstVertex.getValue() + i), i));
    }
  }

  public int updateVertices(int offset, int startIndex)
  {
    ((DecNumber)getAttribute(WED_POLY_VERTEX_INDEX)).setValue(startIndex);
    int count = 0;
    for (final StructEntry entry : getFields()) {
      if (entry instanceof Vertex) {
        entry.setOffset(offset);
        ((AbstractStruct)entry).realignStructOffsets();
        offset += 4;
        count++;
      }
    }
    ((DecNumber)getAttribute(WED_POLY_NUM_VERTICES)).setValue(count);
    return count;
  }

  //<editor-fold defaultstate="collapsed" desc="Readable">
  @Override
  public int read(ByteBuffer buffer, int offset) throws Exception
  {
    addField(new DecNumber(buffer, offset, 4, WED_POLY_VERTEX_INDEX));
    addField(new SectionCount(buffer, offset + 4, 4, WED_POLY_NUM_VERTICES, Vertex.class));
    addField(new Flag(buffer, offset + 8, 1, WED_POLY_FLAGS, s_flags));
    addField(new Unknown(buffer, offset + 9, 1));
    addField(new DecNumber(buffer, offset + 10, 2, WED_POLY_MIN_COORD_X));
    addField(new DecNumber(buffer, offset + 12, 2, WED_POLY_MAX_COORD_X));
    addField(new DecNumber(buffer, offset + 14, 2, WED_POLY_MIN_COORD_Y));
    addField(new DecNumber(buffer, offset + 16, 2, WED_POLY_MAX_COORD_Y));
    return offset + 18;
  }
  //</editor-fold>
}
