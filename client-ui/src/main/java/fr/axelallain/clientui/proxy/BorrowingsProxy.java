package fr.axelallain.clientui.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "borrowings", url = "localhost:9002")
@RibbonClient(name = "borrowings")
public interface BorrowingsProxy {
}
