
entryPrintString:
    li $t0, 4
    add $a0, $a0, $t0
	li $v0, 4
	syscall
exitPrintString:
	j $ra
