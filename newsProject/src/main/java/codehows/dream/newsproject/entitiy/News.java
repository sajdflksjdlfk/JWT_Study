package codehows.dream.newsproject.entitiy;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class News {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "news_id")
	private Long id;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String text;

	@Column(columnDefinition = "TEXT")
	private String summary;

	private Timestamp createAt;

	private Timestamp updateAt;
}
