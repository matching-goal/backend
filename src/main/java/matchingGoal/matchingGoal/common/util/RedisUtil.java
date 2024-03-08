package matchingGoal.matchingGoal.common.util;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {
    private final StringRedisTemplate stringRedisTemplate;

    public String getData(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setData(String key, String value, long durationInMinutes) {
        stringRedisTemplate.opsForValue().set(key, value, durationInMinutes, TimeUnit.MINUTES);
    }

    public void deleteData(String key){
        stringRedisTemplate.delete(key);
    }

    public boolean hasKey(String email) {
        Boolean keyExists = stringRedisTemplate.hasKey(email);
        return keyExists != null && keyExists;
    }
}
