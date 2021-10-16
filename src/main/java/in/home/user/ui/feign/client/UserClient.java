package in.home.user.ui.feign.client;

import in.home.user.api.service.UserService;
import in.home.user.ui.feign.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
    name = "${feign.user.name}",
    url = "${feign.user.url}",
    configuration = FeignConfiguration.class)
public interface UserClient extends UserService {}
