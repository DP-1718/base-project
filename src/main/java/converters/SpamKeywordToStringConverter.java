package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.SpamKeyword;

@Component
@Transactional
public class SpamKeywordToStringConverter implements Converter<SpamKeyword, String> {

	@Override
	public String convert(final SpamKeyword spamkeyword) {

		String result;
		
		if (spamkeyword == null) {
			result = null;
		} else {
			result = String.valueOf(spamkeyword.getId());
		}
		
		return result;
	}

}