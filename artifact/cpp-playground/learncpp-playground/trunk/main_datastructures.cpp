#include <stdio.h>

// http://www.cplusplus.com/reference/stl/vector/
//	Member functions
//	(constructor)	Construct vector (public member function)
//	(destructor)	Vector destructor (public member function)
//	operator=	Copy vector content (public member function )
//
//	Iterators:
//	begin	Return iterator to beginning (public member type)
//	end	Return iterator to end (public member function)
//	rbegin	Return reverse iterator to reverse beginning (public member function)
//	rend	Return reverse iterator to reverse end (public member function)
//
//	Capacity:
//	size	Return size (public member function)
//	max_size	Return maximum size (public member function)
//	resize	Change size (public member function)
//	capacity	Return size of allocated storage capacity (public member function)
//	empty	Test whether vector is empty (public member function)
//	reserve	Request a change in capacity (public member function)
//
//	Element access:
//	operator[]	Access element (public member function)
//	at	Access element (public member function)
//	front	Access first element (public member function)
//	back	Access last element (public member function)
//
//	Modifiers:
//	assign	Assign vector content (public member function)
//	push_back	Add element at the end (public member function)
//	pop_back	Delete last element (public member function)
//	insert	Insert elements (public member function)
//	erase	Erase elements (public member function)
//	swap	Swap content (public member function)
//	clear	Clear content (public member function)
//
//	Allocator:
//	get_allocator	Get allocator (public member function )

#include <vector>
void playingWithVector() {
	std::vector<int> list;
	list.push_back(21);
	list.push_back(42);
	printf("list.size=%i\n", (int) list.size());

	for(int i = 0, n = list.size(); i < n; i++) {
		printf("\t%i = [%i]\n", i, list.at(i));
	}

	list.clear();
	printf("list.size=%i\n", (int) list.size());
}

// http://www.cplusplus.com/reference/stl/map/
//	Member functions
//	(constructor)	Construct map (public member function)
//	(destructor)	Map destructor (public member function)
//	operator=	Copy container content (public member function)
//
//	Iterators:
//	begin	Return iterator to beginning (public member function)
//	end	Return iterator to end (public member function)
//	rbegin	Return reverse iterator to reverse beginning (public member function)
//	rend	Return reverse iterator to reverse end (public member function)
//
//	Capacity:
//	empty	Test whether container is empty (public member function)
//	size	Return container size (public member function)
//	max_size	Return maximum size (public member function)
//
//	Element access:
//	operator[]	Access element (public member function)
//
//	Modifiers:
//	insert	Insert element (public member function)
//	erase	Erase elements (public member function)
//	swap	Swap content (public member function)
//	clear	Clear content (public member function)
//
//	Observers:
//	key_comp	Return key comparison object (public member function)
//	value_comp	Return value comparison object (public member function)
//
//	Operations:
//	find	Get iterator to element (public member function)
//	count	Count elements with a specific key (public member function)
//	lower_bound	Return iterator to lower bound (public member function)
//	upper_bound	Return iterator to upper bound (public member function)
//	equal_range	Get range of equal elements (public member function)
//
//	Allocator:
//	get_allocator	Get allocator (public member function)
#include <map>
void playingWithMap() {
	std::map<int, int> m;

	m[21] = 42;
	m.insert(std::pair<int, int>(42, 84)); // m[42] = 84;

	for(std::map<int, int>::iterator i = m.begin(), end = m.end(); i != end; i++) {
		int key = i->first;
		int val = i->second;
		printf("\t[%i] => [%i]\n", key, val);
	}
}
int main() {
//	playingWithVector();
	playingWithMap();

	return 0;
}

