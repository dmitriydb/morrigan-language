import ru.shanalotte.serviceregistry.client.ServiceRegistryClient;
import ru.shanalotte.serviceregistry.client.SpringBootBasedServiceRegistryClient;

public class TestApp {

  public static void main(String[] args) {
    ServiceRegistryClient serviceRegistryClient = new SpringBootBasedServiceRegistryClient();
    serviceRegistryClient.startWorking("a", "abca", 33);
  }
}
