
entryReadBool:
	la $a0, buffer
    li $a1, 3
    li $v0, 8
    syscall
    lb $v0, buffer
	li $t1, 84
	seq $v0, $v0, $t1
exitReadBool:
	j $ra
