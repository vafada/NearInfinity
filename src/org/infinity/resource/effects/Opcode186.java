// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2022 Jon Olav Hauglid
// See LICENSE.txt for license information

package org.infinity.resource.effects;

import java.nio.ByteBuffer;
import java.util.List;

import org.infinity.datatype.Bitmap;
import org.infinity.datatype.Datatype;
import org.infinity.datatype.DecNumber;
import org.infinity.datatype.IdsBitmap;
import org.infinity.datatype.IdsFlag;
import org.infinity.resource.AbstractStruct;
import org.infinity.resource.Profile;
import org.infinity.resource.ResourceFactory;
import org.infinity.resource.StructEntry;

/**
 * Implemention of opcode 186.
 */
public class Opcode186 extends BaseOpcode {
  private static final String EFFECT_DELAY        = "Delay";
  private static final String EFFECT_ORIENTATION  = "Orientation";
  private static final String EFFECT_ACTION       = "Action";
  private static final String EFFECT_STATE        = "State";

  private static final String RES_TYPE = "ARE";

  private static final String[] ACTIONS_PST = { "Clear", "Set" };

  /** Returns the opcode name for the current game variant. */
  private static String getOpcodeName() {
    switch (Profile.getEngine()) {
      case BG1:
        return "DestroySelf() on target";
      case PST:
        return "Set state";
      default:
        return "Move creature";
    }
  }

  public Opcode186() {
    super(186, getOpcodeName());
  }

  @Override
  protected String makeEffectParamsGeneric(Datatype parent, ByteBuffer buffer, int offset, List<StructEntry> list,
      boolean isVersion1) {
    list.add(new DecNumber(buffer, offset, 4, EFFECT_DELAY));
    list.add(new Bitmap(buffer, offset + 4, 4, EFFECT_ORIENTATION, AbstractStruct.OPTION_ORIENTATION));
    return RES_TYPE;
  }

  @Override
  protected String makeEffectParamsBG1(Datatype parent, ByteBuffer buffer, int offset, List<StructEntry> list,
      boolean isVersion1) {
    list.add(new DecNumber(buffer, offset, 4, AbstractStruct.COMMON_UNUSED));
    list.add(new DecNumber(buffer, offset + 4, 4, AbstractStruct.COMMON_UNUSED));
    return null;
  }

  @Override
  protected String makeEffectParamsEE(Datatype parent, ByteBuffer buffer, int offset, List<StructEntry> list,
      boolean isVersion1) {
    if (ResourceFactory.resourceExists("DIR.IDS")) {
      list.add(new DecNumber(buffer, offset, 4, EFFECT_DELAY));
      list.add(new IdsBitmap(buffer, offset + 4, 4, EFFECT_ORIENTATION, "DIR.IDS"));
      return RES_TYPE;
    } else {
      return makeEffectParamsGeneric(parent, buffer, offset, list, isVersion1);
    }
  }

  @Override
  protected String makeEffectParamsIWD(Datatype parent, ByteBuffer buffer, int offset, List<StructEntry> list,
      boolean isVersion1) {
    list.add(new DecNumber(buffer, offset, 4, EFFECT_DELAY));
    list.add(new IdsBitmap(buffer, offset + 4, 4, EFFECT_ORIENTATION, "DIR.IDS"));
    return RES_TYPE;
  }

  @Override
  protected String makeEffectParamsIWD2(Datatype parent, ByteBuffer buffer, int offset, List<StructEntry> list,
      boolean isVersion1) {
    return makeEffectParamsIWD(parent, buffer, offset, list, isVersion1);
  }

  @Override
  protected String makeEffectParamsPST(Datatype parent, ByteBuffer buffer, int offset, List<StructEntry> list,
      boolean isVersion1) {
    list.add(new Bitmap(buffer, offset, 4, EFFECT_ACTION, ACTIONS_PST));
    list.add(new IdsFlag(buffer, offset + 4, 4, EFFECT_STATE, "STATE.IDS"));
    return null;
  }
}
