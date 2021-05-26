package edu.eci.escuelaing.StripesLink.Cache;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import edu.eci.escuelaing.StripesLink.Model.Ronda;

@Repository
public class UsersCacheRedis implements ICacheRedis {

	public static final String Key = "edu:eci:escuelaing:StripesLink:User";
	public static final String Key2 = "edu:eci:escuelaing:StripesLink:Game";

	private HashOperations<String, String, Long> hashOperations;
	private HashOperations<String, String, Ronda> hashOperations2;

	private RedisTemplate redisTemplate;

	public UsersCacheRedis(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.hashOperations = this.redisTemplate.opsForHash();
		this.hashOperations2 = this.redisTemplate.opsForHash();
	}

	@Override
	public void incrementUsers(String idSala) {
		Long valor = hashOperations.get(Key, idSala);
		if (valor == null) {
			System.out.println("----Entrooo");
			hashOperations.put(Key, idSala, 0L);
		} else {
			hashOperations.put(Key, idSala, valor + 1L);
		}
	}

	@Override
	public void decrementtUsers(String idSala) {
		Long valor = hashOperations.get(Key, idSala);
		if (valor != null) {
			hashOperations.put(Key, idSala, valor - 1L);
		}
	}

	@Override
	public Map<String, Long> getUsers() {
		return hashOperations.entries(Key);
	}

	@Override
	public void setState(Ronda ronda, String idSala) {
		hashOperations2.put(Key2, idSala, ronda);
	}

	@Override
	public Ronda getState(String idSala) {
		return hashOperations2.get(Key2, idSala);
	}
}
