
entryReadChar:
	la $a0, buffer
	li $a1, 3
	li $v0, 8
	syscall
	lb $v0, buffer
exitReadChar:
	j $ra
