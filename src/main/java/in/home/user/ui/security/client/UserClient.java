package in.home.user.ui.security.client;

import in.home.user.api.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${feign.user.name}", url = "${feign.user.url}")
public interface UserClient extends UserService {}
