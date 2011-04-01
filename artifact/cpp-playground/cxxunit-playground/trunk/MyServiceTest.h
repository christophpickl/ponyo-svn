#ifndef MYSERVICETEST_H_
#define MYSERVICETEST_H_

#include <stdio.h>
#include <cxxtest/TestSuite.h>
#include "MyService.h"

/*
 * TS_ASSERT(expr): Verifies that the expression expr evaluates to true
 * TS_ASSERT_EQUALS(x, y): Verifies that the expressions x and y are equal
 * TS_ASSERT_DIFFERS(x, y): Verifies that the expressions x and y are not equal
 * TS_ASSERT_LESS_THAN(x, y): Verifies that the expression x is less than y
 * TS_ASSERT_LESS_THAN_EQUALS(x, y): Verifies that the expression x is less than or equal to y
 */
class MyServiceTest : public CxxTest::TestSuite {
public:
	MyService* testee;

	void setUp() {
		printf("setUp()\n");
		this->testee = new MyService();
	}

	void tearDown() {
		printf("tearDown()\n");
		delete this->testee;
	}

	void testAdd2() {
		printf("testAdd2()\n");
		TS_ASSERT_EQUALS(this->testee->add2(0), 2);
	}
};

#endif /*MYSERVICETEST_H_*/
