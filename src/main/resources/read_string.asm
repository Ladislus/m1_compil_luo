
entryReadString:
    addi $a0, $a0, 4
	li $v0, 8
	syscall
exitReadString:
	j $ra
