package lark.domain.message.contact;

import java.io.Serializable;
import java.util.List;

import lark.domain.Contact;
import lombok.Getter;
import lombok.Setter;

public class ListContactRespBody implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private int totalRecordNumber;
	@Getter @Setter private List<Contact> data;
}
