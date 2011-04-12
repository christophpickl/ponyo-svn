#include <stdio.h>
#include "SenderCallbacking.hpp"

class Handler {
public:
	void doit() {
		SenderCallbacking<Handler> s(this, &Handler::onFoo);
		s.trigger();
	}
private:
	void onFoo() {
		printf("XXXXXXXX JAAA onFoo()\n");
	}
};

int main() {
	printf("main() START!\n");
	Handler h;
	h.doit();
	return 0;
}
