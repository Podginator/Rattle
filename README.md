# Rattle

Rattle is a dynamically typed interpreted language based loosely on python syntax. It is the result of my language design module at The University of Derby.

## Syntax Examples

The following is an example program written in rattle.

```
// Test multiple inheritance. Hello Extends Test extends First Test
class firstTest {
    def printGoodbye() {
        write "Can you save my heavydirtysoul?"
    }
}

// Test multiple inheritance
class test extends firstTest {
	testing = "testoutter"
	// Tests whether we can have two indepent references in classes
	// With the same name (We shouldn't be able to, super->counter == this->counter)
	counter = 0

    // Test when a method is only available in
	def decrementAndGetCounter() {
	    // Tests stacking inside a method
        def decrementCounter() {
            return counter - 1
        }

        // tests writing out to a variable
        this->counter = decrementCounter()
        return counter
    }

    def operateOnCounter(x, y) {
        return x(this->counter) + y
    }

    // Add Two Numbers, one of which is named the same as
    // a member. This tests that we can effectively distinguish
    // between the two variables.
    def addNumbers(a, counter) {
        return a + counter
    }

    // Increment a counter by 20, overriden in hello
    // to be one.
    def incrementAndGetCounter() {
        counter = counter + 20
        return counter
    }

    // This prints something different than super->printGoodbye
    def printGoodbye() {
        write "Goodbye"
    }
}

class hello extends test {
    // Tests that we can effectively
    // define classes in classes.
	class testInnerTwo {
		name = "innertwo"
		test = {create test}
	}
	
	class testInnerClass {
		name = "TestInner"
	}
	
	name = "TR"
	date = "20/12/92"
	counter = 0
	inner = {create testInnerClass}
	
	class Two {
		name = "TestInner"
	}

	def getInnerClassValue() {
		return inner->name
	}

	def getOutterClassValue() {
		x = {create test}
		return x->testing
	}

	def incrementAndGetCounter() {
		def incrementCounter() {
			return counter + 1
		}
		counter = incrementCounter()

		return counter
	}

	def doOriginalIncrement() {
	    counter = super->incrementAndGetCounter()
	    return counter
	}

	def printGoodbye() {
	    write "Top Layer Goodbye!"
	}

	def printHelloaa(c) {
	    return c
	}
}

i = 7
i = {create hello where name="ey!"}

write i->name

// This simple lambda returns two things, so to get both values from it we'd have to say a,b =
simpleLambda = (x,y) -> { return y + x, x / y }
a,b = simpleLambda(1,2)
write "return 1 of simpleLambda: " + a
write "return 2 of simple Lambda: " + b

// This just proves we can do this without functions
c,d,e,f = 12, "T",  "O", "M"
write "c = " + c
write "d = " + d
write "e = " + e
write "f = " + f

// This just proves we can do this with member variables
i->name, i->inner->name = d, e
write "i->name = " + i->name
write "i->inner->name = " + i->inner->name

lambdaA = (x)->{ return x * 4 }
lambdaB = (x)->{ return x * 8 }
lambdaC = (x)->{ return x * 16 }
lambdaD = lambdaA + lambdaB + lambdaC

i->incrementAndGetCounter()
write "i->operateOnCounter(x,y) where x =  lambda curried to do x * 4 * 8 * 16, counter = 1 and y = 0.  So 512 : "
            + i->operateOnCounter(lambdaD, 0)


multiLambdaA = (x,y)->{return x * 2, y /2 }
multiLambdaB = (x,y)->{return x * 4, y / 4 } 
multiLambdaD = multiLambdaA + multiLambdaB

resultA, resultB = multiLambdaD(1,2)

```

## Built With

* JavaCC The web framework used

## Acknowledgments

* Dave Voorhis, Lecturer at the University of Derby, and general Cat Lover. 
