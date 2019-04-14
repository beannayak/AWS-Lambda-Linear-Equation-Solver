# AWS-Lambda-Linear-Equation-Solver
* What is this project ?
    - This project Java based implementation for solving linear-equations using LINDO native library.
    - This project demonstrates how a java project can be implemented with using native libraries in AWS-Lambda.

* A sample request:
    - Request
    { 
	"direction": "MINIMIZE",
	"objectiveFunction": "1.0_b",
	"constraints": [ 
		"1.0_a;1.0_b=10.0",
		"1.0_a<5.0"
	] 
    }
