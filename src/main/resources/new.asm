
entryNew:
    mul $t0, $a0, $a1
    li $t1, 4
    add $t0, $t0, $t1
    move $a0, $t0
    li $v0, 9
	syscall
	sw $a1, ($v0)
exitNew:
	j $ra