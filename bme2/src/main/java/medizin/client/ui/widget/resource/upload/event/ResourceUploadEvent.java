package medizin.client.ui.widget.resource.upload.event;

import java.util.List;

import medizin.client.util.ClientUtility;
import medizin.shared.MultimediaType;
import medizin.shared.utils.SharedConstant;
import medizin.shared.utils.SharedUtility;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.gwt.event.shared.GwtEvent;

public class ResourceUploadEvent extends GwtEvent<ResourceUploadEventHandler> {

	private final boolean resourceUploaded;
	private String filePath;
	private MultimediaType type;
	private Integer width;
	private Integer height;
	private Integer soundMediaSize;
	private Integer videoMediaSize;
	
	public ResourceUploadEvent(String data, Boolean resourceUploaded) {
		this.resourceUploaded = resourceUploaded;
		if(data != null && data.length() > 0)
			parseData(data);
	}
	
	
	private void parseData(String data) {
		data = data.replace("{", "").replace("}", "");
		FluentIterable<String> fluentIterable = FluentIterable.from(Splitter.on(",").omitEmptyStrings().trimResults().split(data));
		
		filePath = parseFilePath(fluentIterable);
		type = SharedUtility.getFileMultimediaType(SharedUtility.getFileExtension(filePath));
		width = parseImageWidth(fluentIterable);
		height = parseImageHeight(fluentIterable);
		soundMediaSize = parseSoundMediaSize(fluentIterable);
		videoMediaSize = parseVideoMediaSize(fluentIterable);
	}

	public static Type<ResourceUploadEventHandler> TYPE = new Type<ResourceUploadEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ResourceUploadEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ResourceUploadEventHandler handler) {
		handler.onResourceUploaded(this);		
	}

	public boolean isResourceUploaded() {
		return resourceUploaded;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public MultimediaType getType() {
		return type;
	}

	public Integer getWidth() {
		return width;
	}

	public Integer getHeight() {
		return height;
	}

	public Integer getSoundMediaSize() {
		return soundMediaSize;
	}

	public Integer getVideoMediaSize() {
		return videoMediaSize;
	}
	
	private Integer parseVideoMediaSize(FluentIterable<String> fluentIterable) {
		return parseValueToInteger(fluentIterable, vidoeSizePredicate);
	}


	private Integer parseSoundMediaSize(FluentIterable<String> fluentIterable) {
		return parseValueToInteger(fluentIterable, soundSizePredicate);
	}


	private Integer parseImageHeight(FluentIterable<String> fluentIterable) {
		return parseValueToInteger(fluentIterable, heightPredicate);
	}


	private Integer parseImageWidth(FluentIterable<String> fluentIterable) {
		return parseValueToInteger(fluentIterable, widthPredicate);
	}


	private String parseFilePath(FluentIterable<String> fluentIterable) {
		return parseValueToString(fluentIterable, filepathPredicate);
	}
	
	private String parseValueToString(FluentIterable<String> fluentIterable,Predicate<String> predicate) {
		FluentIterable<String> iterable = fluentIterable.filter(predicate).transform(splitToValues);
		if(iterable != null && !iterable.isEmpty()) {
			return iterable.get(0);
		}
		else { 
			return null;
		}
	}
	
	private Integer parseValueToInteger(FluentIterable<String> fluentIterable,Predicate<String> predicate) {
		String value = parseValueToString(fluentIterable, predicate);
		
		if(value != null && ClientUtility.isNumber(value)) {
			return Integer.parseInt(value,10);
		}
		return null;
	}
	
	
	// predicates
	private final Predicate<String> filepathPredicate =  new Predicate<String>() {

		@Override
		public boolean apply(String input) {
			return input.contains(SharedConstant.FILEPATH);
		}
	};

	private final Predicate<String> widthPredicate =  new Predicate<String>() {

		@Override
		public boolean apply(String input) {
			return input.contains(SharedConstant.WIDTH);
		}
	};

	private final Predicate<String> heightPredicate =  new Predicate<String>() {

		@Override
		public boolean apply(String input) {
			return input.contains(SharedConstant.HEIGHT);
		}
	};

	private final Predicate<String> soundSizePredicate =  new Predicate<String>() {

		@Override
		public boolean apply(String input) {
			return input.contains(SharedConstant.SOUND_MEDIA_SIZE);
		}
	};
	
	private final Predicate<String> vidoeSizePredicate =  new Predicate<String>() {

		@Override
		public boolean apply(String input) {
			return input.contains(SharedConstant.VIDEO_MEDIA_SIZE);
		}
	};

	private final Function<String, String> splitToValues = new Function<String, String>() {
		
		@Override
		public String apply(String input) {
		
			if(input == null || input.isEmpty()){
				return null;
			}
			else {
				List<String> list = Lists.newArrayList(Splitter.on("=").omitEmptyStrings().trimResults().split(input));
				if(list.size() == 2) {
					return list.get(1);
				}else {
					return null;
				}
				
			}
		}
	};
	
}
