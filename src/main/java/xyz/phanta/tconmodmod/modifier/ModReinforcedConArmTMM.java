package xyz.phanta.tconmodmod.modifier;

import c4.conarm.lib.modifiers.ArmorModifierTrait;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.TinkerModifiers;
import xyz.phanta.tconmodmod.TMMConfig;

import java.util.List;

// adapted from Construct's Armory ModReinforced, which is under LGPLv3
// (c) 2018 TheIllusiveC4
// changes not stated because code was adapted from rather than copied verbatim
// https://github.com/TheIllusiveC4/ConstructsArmory/blob/master/src/main/java/c4/conarm/common/armor/modifiers/ModReinforced.java
public class ModReinforcedConArmTMM extends ArmorModifierTrait {

    public ModReinforcedConArmTMM() {
        super("reinforced", 0x502E83, 5, 0); // can be changed by mutators so no config options are needed
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag) {
        super.applyEffect(rootCompound, modifierTag);
        if (getReinforcedChance(modifierTag) >= 1F) {
            rootCompound.setBoolean("Unbreakable", true);
        }
    }

    @Override
    public int onArmorDamage(ItemStack armor, DamageSource source, int damage, int newDamage, EntityPlayer player, int slot) {
        if (player.world.isRemote) {
            return 0;
        }
        if (random.nextFloat() <= getReinforcedChance(TinkerUtil.getModifierTag(armor, identifier))) {
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
            return Util.translate("modifier.%s.unbreakable", TinkerModifiers.modReinforced.getIdentifier());
        }
        return super.getTooltip(modifierTag, detailed);
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        String key = String.format("modifier.%s.extra", getIdentifier());
        if (I18n.hasKey(key)) {
            float chance = getReinforcedChance(modifierTag);
            return ImmutableList.of(Util.translateFormatted(key, chance >= 1F
                    ? Util.translate("modifier.%s.unbreakable", TinkerModifiers.modReinforced.getIdentifier())
                    : Util.dfPercent.format(chance)));
        }
        return super.getExtraInfo(tool, modifierTag);
    }

    private static float getReinforcedChance(NBTTagCompound modifierTag) {
        return ModifierNBT.readTag(modifierTag).level * getChancePerLevel();
    }

    private static float getChancePerLevel() {
        return (float)TMMConfig.reinforcedConArmConfig.chancePerLevel;
    }

}
