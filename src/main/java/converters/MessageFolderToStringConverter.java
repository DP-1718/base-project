package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.MessageFolder;

@Component
@Transactional
public class MessageFolderToStringConverter implements Converter<MessageFolder, String> {

	@Override
	public String convert(final MessageFolder messagefolder) {

		String result;
		
		if (messagefolder == null) {
			result = null;
		} else {
			result = String.valueOf(messagefolder.getId());
		}
		
		return result;
	}

}