package scott.spikeforawarnessapi;

import android.content.*;
import android.net.*;

import com.google.auto.value.*;

import static android.content.Intent.*;

@AutoValue
public abstract class EmailIntent {

    public static AutoValue_EmailIntent.Builder builder() {
        return new AutoValue_EmailIntent.Builder();
    }

    public abstract String email();
    public abstract String subject();
    public abstract String body();
    public abstract Context context();

    public void send() {
        Intent intent = new Intent(ACTION_SENDTO, Uri.fromParts("mailto", email(), null));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject());
        intent.putExtra(Intent.EXTRA_TEXT, body());
        context().startActivity(intent);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setContext(Context value);
        public abstract Builder setEmail(String value);
        public abstract Builder setSubject(String value);
        public abstract Builder setBody(String value);
        public abstract EmailIntent build();
    }
}
