package in.home.user.ui;

import java.io.Serializable;
import java.time.LocalTime;
import org.springframework.stereotype.Service;

@Service
public class MessageBean implements Serializable {

  public static final long serialVersionUID = 1L;

  public String getMessage() {
    return "Button was clicked at " + LocalTime.now();
  }
}
