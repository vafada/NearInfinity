// Near Infinity - An Infinity Engine Browser and Editor
// Copyright (C) 2001 - 2022 Jon Olav Hauglid
// See LICENSE.txt for license information

package org.infinity.resource.effects;

import java.nio.ByteBuffer;
import java.util.List;

import org.infinity.datatype.Bitmap;
import org.infinity.datatype.Datatype;
import org.infinity.datatype.DecNumber;
import org.infinity.resource.AbstractStruct;
import org.infinity.resource.Profile;
import org.infinity.resource.StructEntry;

/**
 * Implemention of opcode 341.
 */
public class Opcode341 extends BaseOpcode {
  private static final String EFFECT_CONDITION    = "Condition";
  private static final String EFFECT_ATTACK_TYPE  = "Attack type";

  private static final String RES_TYPE = "SPL";

  private static final String[] CONDITIONS = { "Always", "By this weapon only" };
  private static final String[] ATTACK_TYPES_EE = { "Any attack type", "Melee attack only", "Ranged attack only",
      "Magical attack only" };

  /** Returns the opcode name for the current game variant. */
  private static String getOpcodeName() {
    switch (Profile.getEngine()) {
      case EE:
        return "Critical hit effect";
      default:
        return null;
    }
  }

  public Opcode341() {
    super(341, getOpcodeName());
  }

  @Override
  protected String makeEffectParamsEE(Datatype parent, ByteBuffer buffer, int offset, List<StructEntry> list,
      boolean isVersion1) {
    list.add(new DecNumber(buffer, offset, 4, AbstractStruct.COMMON_UNUSED));
    list.add(new Bitmap(buffer, offset + 4, 4, EFFECT_CONDITION, CONDITIONS));
    return RES_TYPE;
  }

  @Override
  protected int makeEffectSpecial(Datatype parent, ByteBuffer buffer, int offset, List<StructEntry> list,
      String resType, int param1, int param2) {
    if (Profile.isEnhancedEdition()) {
      list.add(new Bitmap(buffer, offset, 4, EFFECT_ATTACK_TYPE, ATTACK_TYPES_EE));
      return offset + 4;
    } else {
      return super.makeEffectSpecial(parent, buffer, offset, list, resType, param1, param2);
    }
  }
}
