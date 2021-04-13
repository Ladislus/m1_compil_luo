
entryPrintBool:
    bne $a0, 0, printBoolTrue
    j printBoolFalse
printBoolTrue:
	li $a0, 84
	li $v0, 11
	syscall
	j exitPrintBool
printBoolFalse:
    li $a0, 70
    li $v0, 11
    syscall
exitPrintBool:
	j $ra
