package lark.domain.message.chat;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SingleChatReqBody implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String ticket;
	@Getter @Setter private String toUserId;
	/**
	 * 100表示纯文本，可包含表情
	 * 200表示文件，210表示图片，220表示音频，230表示视频
	 * 300表示模板
	 */
	@Getter @Setter private int contentType;
	//这是一个json表示的字符串，服务端会根据contentType进行content的格式转换
	@Getter @Setter private String content;
}
