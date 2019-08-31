package xyz.phanta.tconmodmod.modifier;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.modifiers.ModReinforced;
import xyz.phanta.tconmodmod.TMMConfig;

import java.util.List;

// adapted from Tinkers' Construct ModReinforced, which is under MIT
// https://github.com/SlimeKnights/TinkersConstruct/blob/1.12/src/main/java/slimeknights/tconstruct/tools/modifiers/ModReinforced.java
public class ModReinforcedTMM extends ModifierTrait {

    public ModReinforcedTMM() {
        super("reinforced", 0x502E83, 5, 0); // can be changed by mutators so no config options are needed
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);
        if (getReinforcedChance(modifierTag) >= 1F) {
            rootCompound.setBoolean(ModReinforced.TAG_UNBREAKABLE, true);
        }
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        if (entity.world.isRemote) {
            return 0;
        }
        if (random.nextFloat() <= getReinforcedChance(TinkerUtil.getModifierTag(tool, identifier))) {
            newDamage -= damage;
        }
        return Math.max(0, newDamage);
    }

    @Override
    public String getLocalizedDesc() {
        return String.format(super.getLocalizedDesc(), Util.dfPercent.format(getChancePerLevel()));
    }

    @Override
    public String getTooltip(NBTTagCompound modifierTag, boolean detailed) {
        if (getReinforcedChance(modifierTag) >= 1F) {
            return Util.translate("modifier.%s.unbreakable", getIdentifier());
        }
        return super.getTooltip(modifierTag, detailed);
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        String key = String.format(LOC_Extra, getIdentifier());
        if (I18n.hasKey(key)) {
            float chance = getReinforcedChance(modifierTag);
            return ImmutableList.of(Util.translateFormatted(key, chance >= 1F
                    ? Util.translate("modifier.%s.unbreakable", getIdentifier())
                    : Util.dfPercent.format(chance)));
        }
        return super.getExtraInfo(tool, modifierTag);
    }

    private static float getReinforcedChance(NBTTagCompound modifierTag) {
        return ModifierNBT.readTag(modifierTag).level * getChancePerLevel();
    }

    private static float getChancePerLevel() {
        return (float)TMMConfig.reinforcedConfig.chancePerLevel;
    }

}
