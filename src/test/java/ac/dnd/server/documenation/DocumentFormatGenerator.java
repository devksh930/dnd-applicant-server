package ac.dnd.server.documenation;

import static org.springframework.restdocs.snippet.Attributes.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.restdocs.snippet.Attributes;

public class DocumentFormatGenerator {
	public DocumentFormatGenerator() {
		throw new UnsupportedOperationException("Util class.");
	}

	public static Attributes.Attribute required() {
		return key("required").value("true");
	}

	public static Attributes.Attribute optional() {
		return key("required").value("false");
	}

	public static Attributes.Attribute customFormat(String format) {
		return key("format").value(format);
	}

	public static Attributes.Attribute emptyFormat() {
		return customFormat("");
	}

	public static Attributes.Attribute dateTimeFormat() {
		return key("format").value("yyyy-MM-dd HH:mm:ss");
	}

	public static Attributes.Attribute dateFormat() {
		return key("format").value("yyyy-MM-dd");
	}

	public static Attributes.Attribute timeFormat() {
		return key("format").value("HH:mm:ss");
	}

	public static <E extends Enum<E>> String generatedEnums(Class<E> enumClass) {
		return Arrays.stream(enumClass.getEnumConstants())
			.map(el -> "* `" + el.name() + "`")
			.collect(Collectors.joining("\n"));
	}

	public static <E extends Enum<E>> Attributes.Attribute generateEnumAttrs(
		Class<E> enumClass,
		Function<E, String> detailFun
	) {
		var value = Arrays.stream(enumClass.getEnumConstants())
			.map(el -> "* `" + el.name() + "`(" + detailFun.apply(el) + ")")
			.collect(Collectors.joining("\n"));
		return key("format").value(value);
	}

	public static <E extends Enum<E>> Attributes.Attribute generateEnumListFormatAttribute(
		List<E> enumClassList,
		Function<E, String> detailFun
	) {
		var value = enumClassList.stream()
			.map(el -> "* `" + el.name() + "`(" + detailFun.apply(el) + ")")
			.collect(Collectors.joining("\n"));

		return key("format").value(value);
	}
}
