package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.MessageFolderRepository;
import domain.MessageFolder;

@Component
@Transactional
public class StringToMessageFolderConverter implements Converter<String, MessageFolder> {

	@Autowired
	MessageFolderRepository	messagefolderRepository;


	@Override
	public MessageFolder convert(final String text) {
		MessageFolder result;
		int id;

		try {
		
			if (StringUtils.isEmpty(text)) {
				result = null;
			} else {
				id = Integer.valueOf(text);
				result = this.messagefolderRepository.findOne(id);
			}
			
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		
		return result;
	}

}
