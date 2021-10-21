package jpastudy.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	@Bean
	Hibernate5Module hibernate5Module() {
		//포스트맨에 http://localhost:8087/api/v1/simple-orders get으로 하면
		//이렇게 하면 의존관계에 있는 name, address, delivery등을 못 가져 온다.
		return new Hibernate5Module();

		//강제로 의존관계에 있는 객체를 가져오게 한다.  FORCE_LAZY_LOADING
		// 단점으로는 쿼리를 엄청 날린다.
//		Hibernate5Module hibernate5Module = new Hibernate5Module();
//		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING,true);
//		return hibernate5Module;

	}

}
