package edu.eci.escuelaing.StripesLink.Cache;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public class UsersCacheRedis implements ICacheRedis {

	public static final String Key = "edu:eci:escuelaing:StripesLink:User";

	private HashOperations<String, String, Long> hashOperations;

	private RedisTemplate redisTemplate;

	public UsersCacheRedis(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.hashOperations = this.redisTemplate.opsForHash();
	}

	@Override
	public void incrementUsers(String idSala) {
		Long valor = hashOperations.get(Key, idSala);
		if (valor == null) {
			System.out.println("----Entrooo");
			hashOperations.put(Key, idSala, 0L);
		} else {
			hashOperations.put(Key, idSala, valor + 1L);
			/*
			 * System.out.println("----Entrooo2"); System.out.println("-------HashValue:" +
			 * hashOperations.get(Key, idSala).getClass()); hashOperations.increment(Key,
			 * idSala, 1); System.out.println("-------Saliooooooo");
			 */
		}
	}

	@Override
	public void decrementtUsers(String idSala) {
		Long valor = hashOperations.get(Key, idSala);
		if (valor == null) {
			hashOperations.put(Key, idSala, valor - 1L);
			//hashOperations.increment(Key, idSala, -1);
		}
	}

	@Override
	public Map<String, Long> get() {
		return hashOperations.entries(Key);
	}

	private String getKey(String idSala) {
		return String.format("%s:%s", Key, idSala);
	}

}
