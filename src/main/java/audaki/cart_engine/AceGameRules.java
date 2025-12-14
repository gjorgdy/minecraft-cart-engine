package audaki.cart_engine;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import org.jetbrains.annotations.NotNull;

public class AceGameRules {

    // All speeds in blocks per second
    private static final String ID_MAX_SPEED_PLAYER_RIDER = "ace:speed_player";
    private static final String ID_MAX_SPEED_OTHER_RIDER  = "ace:speed_other";
    private static final String ID_MAX_SPEED_EMPTY_RIDER  = "ace:speed_empty";
	private static final String ID_HALT_SPEED_THRESHOLD  = "ace:halt_speed_treshold";
	private static final String ID_HALT_SPEED_MULTIPLIER  = "ace:halt_speed_multiplier";

    public static final GameRule<@NotNull Integer> MINECART_MAX_SPEED_PLAYER_RIDER =
            GameRuleBuilder.forInteger(20).buildAndRegister(Identifier.parse(ID_MAX_SPEED_PLAYER_RIDER));

    public static final GameRule<@NotNull Integer> MINECART_MAX_SPEED_OTHER_RIDER =
            GameRuleBuilder.forInteger(0).buildAndRegister(Identifier.parse(ID_MAX_SPEED_OTHER_RIDER));

    public static final GameRule<@NotNull Integer> MINECART_MAX_SPEED_EMPTY_RIDER =
            GameRuleBuilder.forInteger(0).buildAndRegister(Identifier.parse(ID_MAX_SPEED_EMPTY_RIDER));

	public static final GameRule<@NotNull Integer> MINECART_HALT_SPEED_THRESHOLD =
			GameRuleBuilder.forInteger(0).buildAndRegister(Identifier.parse(ID_HALT_SPEED_THRESHOLD));

	public static final GameRule<@NotNull Integer> MINECART_HALT_SPEED_MULTIPLIER =
			GameRuleBuilder.forInteger(0).buildAndRegister(Identifier.parse(ID_HALT_SPEED_MULTIPLIER));

    public static void register() {}
}
