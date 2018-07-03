package eu.zomtec.em2012.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

import eu.zomtec.em2012.domain.Bet;
import eu.zomtec.em2012.domain.BetUser;
import eu.zomtec.em2012.domain.Game;

/**
 * A central place to register application converters and formatters.
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends
		FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
		final GameConverter converter = new GameConverter();
		registry.addConverter(converter);
		final BetConverter converter2 = new BetConverter();
		registry.addConverter(converter2);
		final BetUserConverter converter3 = new BetUserConverter();
		registry.addConverter(converter3);
	}

	static class GameConverter implements Converter<Game, String> {
		@Override
		public String convert(Game game) {
			return String.valueOf(game.getId());
		}

	}

	static class BetConverter implements Converter<Bet, String> {
		@Override
		public String convert(Bet game) {
			return String.valueOf(game.getId());
		}

	}

	static class BetUserConverter implements Converter<BetUser, String> {
		@Override
		public String convert(BetUser game) {
			return String.valueOf(game.getUsername());
		}
	}
}
