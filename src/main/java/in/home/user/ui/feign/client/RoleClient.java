package in.home.user.ui.feign.client;

import in.home.user.api.service.RoleService;
import in.home.user.ui.feign.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
    name = "role-client",
    url = "${feign.user-url}",
    configuration = FeignConfiguration.class)
public interface RoleClient extends RoleService {}
