#include <stdio.h>

typedef void (*FooCallback) ();

class Sender {
public:
	void registerListener(FooCallback callback) {
		printf("Sender.registerListener()\n");
		this->m_callback = callback;
	}
	void trigger() {
		printf("Sender.trigger()\n");
		this->m_callback();
	}
private:
	FooCallback m_callback;
};

class Handler {
public:
	void doit() {
		Sender s;
		s.registerListener(&Handler::onFoo);
		s.trigger();
	}
private:
	static void onFoo() {
		printf("XXXXXXXX onFoo()\n");
	}
};

int main() {
	printf("main() START!\n");

	Handler h;
	h.doit();

	return 0;
}
