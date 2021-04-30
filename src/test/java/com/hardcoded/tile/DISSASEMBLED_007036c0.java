package com.hardcoded.tile;

import java.util.function.Supplier;

/** This class is not thread safe.
 *
 * This class was generated by a ghidra script made by
 * HardCoded - https://github.com/Kariaro
 *
 * Information:
 * Date: Thu Jul 02 20:38:02 CEST 2020
 *
 * BRANCHES     = 24
 * INSTRUCTIONS = 294
 *
 */
class DISSASEMBLED_007036c0 {
	private int EAX, ECX, EDX, EBX, ESP, EBP, ESI, EDI;
	private byte[] XMM = new byte[128];
	private int PF, ZF, SF, CF, OF; /* ERFLAGS */
	private final int ALLOCATED_MEMORY = 16777216;
	private byte[] MEMORY = new byte[ALLOCATED_MEMORY];
	private int STACK_INDEX = 16;
	
	private void SET_FLAGS(int dst, int src) {
		ZF = (dst == src) ? 1:0;
		CF = (dst < src) ? 1:0;
		SF = (((dst - src) | 0x80000000) > 0) ? 1:0;
		OF = (((dst - src) | 0x80000000) != ((src) | 0x80000000)) ? 1:0;
	}
	
	/**
	 * 1: BYTE ptr<br>
	 * 2: WORD ptr<br>
	 * 3: DWORD ptr
	 */
	private int GET_MEMORY(int offset, int type) {
		if(type == 1) { return (MEMORY[offset] & 0xff); }
		if(type == 2) { return ((MEMORY[offset] & 0xff) << 8) | (MEMORY[offset + 1] & 0xff); }
		if(type == 3) { return (GET_MEMORY(offset, 2) << 16) | (GET_MEMORY(offset + 2, 2)); }
		throw new java.lang.IllegalArgumentException("INVALID MEMORY TYPE '" + type + "'");
	}
	
	/**
	 * 1: BYTE ptr<br>
	 * 2: WORD ptr<br>
	 * 3: DWORD ptr
	 */
	private void SET_MEMORY(int value, int offset, int type) {
		if(type == 1) { MEMORY[offset] = (byte)(value & 0xff); return; }
		if(type == 2) { SET_MEMORY(value & 0xff, offset, 1); SET_MEMORY((value >> 8) & 0xff, offset + 1, 1); return; }
		if(type == 3) { SET_MEMORY(value & 0xffff, offset, 2); SET_MEMORY((value >> 16) & 0xffff, offset + 2, 2); return; }
		throw new java.lang.IllegalArgumentException("INVALID MEMORY TYPE '" + type + "'");
	}
	
	private void _XMM_PTR(int a,int b){for(int i=0,j=a*32;i<32;i++)XMM[j+i]=MEMORY[b+i];}
	private void _XMM_XMM(int a,int b){for(int i=0,j=a*32,k=b*32;i<32;i++)XMM[j+i]=XMM[k+i];}
	private void _PTR_XMM(int a,int b){for(int i=0,j=b*32;i<32;i++)MEMORY[a+i]=XMM[j+i];}
	
	private void PUSH(int value) { SET_MEMORY(value, STACK_INDEX, 3); STACK_INDEX += 4; }
	private int POP() { int result = GET_MEMORY(STACK_INDEX - 4, 3); STACK_INDEX -= 4; return result; }
	
	/**
	 *<pre>0: EAX
	 *1: ECX
	 *2: EDX
	 *3: EBX
	 *4: ESP
	 *5: EBP
	 *6: ESI
	 *7: EDI</pre>
	 */
	public void setRegister(int id, int value) {
		switch(id) {
			case 0: EAX = value; break;
			case 1: ECX = value; break;
			case 2: EDX = value; break;
			case 3: EBX = value; break;
			case 4: ESP = value; break;
			case 5: EBP = value; break;
			case 6: ESI = value; break;
			case 7: EDI = value; break;
		}
	}
	
	/** Reset all fields. */
	public void reset() {
		EAX = ECX = EDX = EBX = ESP = EBP = ESI = EDI = 0;
		PF = ZF = SF = CF = OF = 0;
		MEMORY = new byte[ALLOCATED_MEMORY];
	}
	
	/*
	 * int __fastcall DECOMP_SAME_AS_TILE_007036c0(byte* input, int* output, undefined4 param_1_00, undefined4 size); 
	 * int               EAX:4          <RETURN>
	 * byte *            ECX:4          input
	 * int *             EDX:4          output
	 * undefined4        Stack[0x4]:4   param_1_00
	 * undefined4        Stack[0x8]:4   size
	 */
	
	/**
	 * Run this function to execute the decompiled code.
	 * Remember to set the correct registers before calling.
	 */
	public int run(byte[] input, byte[] output, int param_1_00, int size) {
		ECX = (0x10000) + 1000000 * 0;
		EDX = (0x10000) + 1000000 * 1;
		
		for(int i = 0; i < input.length; i++) MEMORY[ECX + i] = input[i];
		
		ESP = 0x4000;
		SET_MEMORY(param_1_00, ESP + 4, 3);
		SET_MEMORY(size, ESP + 8, 3);
		
		Supplier<Object> func = this::LAB_007036c0;
		while(func != null) {
			Object result = func.get();
			if(result == null) break;
			
			func = (Supplier<Object>)func;
		}

		for(int i = 0; i < output.length; i++) output[i] = MEMORY[((0x10000) + 1000000 * 1) + i];
		
		return EAX;
	}
	
	private Object LAB_007022b0() {
		EAX = (EAX & 0xffff0000) | ((GET_MEMORY(ECX, 2)) & 0xffff);
		return null;
	}

	private Object LAB_007036c0() {
		PUSH(EBP);
		EBP = ESP;
		ESP -= 0x28;
		EAX = ECX;
		SET_MEMORY(EDX, EBP - 0x14, 3);
		SET_MEMORY(EAX, EBP - 0x1c, 3);
		SET_FLAGS(EAX, EAX);
		if(ZF == 0) { // JNZ 0x007036d9
			return (Supplier<Object>)this::LAB_007036d9;
		}
		EAX |= 0xffffffff;
		ESP = EBP;
		EBP = POP();
		return null;
	}

	private Object LAB_007036d9() {
		ECX = GET_MEMORY(EBP + 0x8, 3);
		PUSH(EBX);
		PUSH(ESI);
		PUSH(EDI);
		EDI = EAX + ECX;
		EBX = EAX;
		EAX = GET_MEMORY(EBP + 0xc, 3);
		ESI = EDX;
		EDX += EAX;
		SET_MEMORY(EDI, EBP - 0x8, 3);
		SET_MEMORY(EDX, EBP - 0xc, 3);
		EDI -= 0x10;
		EDX -= 0x20;
		SET_MEMORY(EDI, EBP - 0x24, 3);
		SET_MEMORY(EDX, EBP - 0x20, 3);
		SET_FLAGS(EAX, EAX);
		if(ZF == 0) { // JNZ 0x00703716
			return (Supplier<Object>)this::LAB_00703716;
		}
		SET_FLAGS(ECX, 0x1);
		if(ZF == 0) { // JNZ 0x0070371a
			return (Supplier<Object>)this::LAB_0070371a;
		}
		EAX = EBX;
		SET_FLAGS(GET_MEMORY(EAX, 1), 0x0);
		if(ZF == 0) { // JNZ 0x0070371a
			return (Supplier<Object>)this::LAB_0070371a;
		}
		EDI = POP();
		ESI = POP();
		EAX = 0;
		EBX = POP();
		ESP = EBP;
		EBP = POP();
		return null;
	}

	private Object LAB_00703716() {
		SET_FLAGS(ECX, ECX);
		if(ZF == 0) { // JNZ 0x00703724
			return (Supplier<Object>)this::LAB_00703724;
		}
		return (Supplier<Object>)this::LAB_0070371a;
	}

	private Object LAB_0070371a() {
		EDI = POP();
		ESI = POP();
		EAX |= 0xffffffff;
		EBX = POP();
		ESP = EBP;
		EBP = POP();
		return null;
	}

	private Object LAB_00703724() {
		EDI = GET_MEMORY(EBX, 1);
		EBX++;
		EDX = EDI;
		SET_MEMORY(EDI, EBP - 0x10, 3);
		EDX >>>= 0x4;
		SET_MEMORY(EDX, EBP - 0x4, 3);
		SET_FLAGS(EDX, 0xf);
		if(ZF == 1) { // JZ 0x007037be
			return (Supplier<Object>)this::LAB_007037be;
		}
		SET_FLAGS(GET_MEMORY(EBP - 0x20, 3), ESI);
		ECX -= (ECX + CF);
		ECX++;
		SET_FLAGS(EBX, GET_MEMORY(EBP - 0x24, 3));
		EAX -= (EAX + CF);
		EAX = -EAX;
		SET_FLAGS(EAX, ECX);
		if(ZF == 1) { // JZ 0x007037fd
			return (Supplier<Object>)this::LAB_007037fd;
		}
		_XMM_PTR(0, EBX);
		EBX += EDX;
		EDI &= 0xf;
		ECX = EBX;
		SET_MEMORY(EBX, EBP - 0x18, 3);
		_PTR_XMM(ESI, 0);
		ESI += EDX;
		SET_MEMORY(EDI, EBP - 0x10, 3);
		LAB_007022b0();
		ECX = GET_MEMORY(EBP - 0x10, 3);
		EDI = ESI;
		EAX = (EAX & 0xffff);
		EBX += 0x2;
		EDI -= EAX;
		SET_MEMORY(EAX, EBP - 0x4, 3);
		SET_FLAGS(ECX, 0xf);
		if(ZF == 1) { // JZ 0x0070384c
			return (Supplier<Object>)this::LAB_0070384c;
		}
		SET_FLAGS(EAX, 0x8);
		if(CF == 1) { // JC 0x0070384c
			return (Supplier<Object>)this::LAB_0070384c;
		}
		SET_FLAGS(EDI, GET_MEMORY(EBP - 0x14, 3));
		if(CF == 1) { // JC 0x0070384c
			return (Supplier<Object>)this::LAB_0070384c;
		}
		EAX = GET_MEMORY(EDI, 3);
		SET_MEMORY(EAX, ESI, 3);
		EAX = GET_MEMORY(EDI + 0x4, 3);
		SET_MEMORY(EAX, ESI + 0x4, 3);
		EAX = GET_MEMORY(EDI + 0x8, 3);
		SET_MEMORY(EAX, ESI + 0x8, 3);
		EAX = GET_MEMORY(EDI + 0xc, 3);
		SET_MEMORY(EAX, ESI + 0xc, 3);
		EAX = (EAX & 0xffff0000) | ((GET_MEMORY(EDI + 0x10, 2)) & 0xffff);
		SET_MEMORY((EAX & 0xffff), ESI + 0x10, 2);
		ESI += 0x4;
		ESI += ECX;
		return (Supplier<Object>)this::LAB_00703724; // JMP 0x00703724
	}

	private Object LAB_007037be() {
		ECX = GET_MEMORY(EBP - 0x8, 3);
		EDX = 0;
		ECX -= 0xf;
		SET_FLAGS(EBX, ECX);
		if(CF == 0) { // JNC 0x007039af
			return (Supplier<Object>)this::LAB_007039af;
		}
		return (Supplier<Object>)this::LAB_007037d0;
	}

	private Object LAB_007037d0() {
		EAX = GET_MEMORY(EBX, 1);
		EBX++;
		EDX += EAX;
		SET_FLAGS(EBX, ECX);
		if(CF == 0) { // JNC 0x007037e1
			return (Supplier<Object>)this::LAB_007037e1;
		}
		SET_FLAGS(EAX, 0xff);
		if(ZF == 1) { // JZ 0x007037d0
			return (Supplier<Object>)this::LAB_007037d0;
		}
		return (Supplier<Object>)this::LAB_007037e1;
	}

	private Object LAB_007037e1() {
		EDX += 0xf;
		SET_MEMORY(EDX, EBP - 0x4, 3);
		EAX = EDX + ESI;
		SET_FLAGS(EAX, ESI);
		if(CF == 1) { // JC 0x007039af
			return (Supplier<Object>)this::LAB_007039af;
		}
		EAX = EDX + EBX;
		SET_FLAGS(EAX, EBX);
		if(CF == 1) { // JC 0x007039af
			return (Supplier<Object>)this::LAB_007039af;
		}
		return (Supplier<Object>)this::LAB_007037fd;
	}

	private Object LAB_007037fd() {
		EAX = GET_MEMORY(EBP - 0xc, 3);
		EDI = EDX + ESI;
		EAX -= 0xc;
		ECX = EDX + EBX;
		SET_MEMORY(ECX, EBP - 0x18, 3);
		SET_FLAGS(EDI, EAX);
		if(CF == 0 && ZF == 0) { // JA 0x0070398b
			return (Supplier<Object>)this::LAB_0070398b;
		}
		EAX = GET_MEMORY(EBP - 0x8, 3);
		EAX -= 0x8;
		SET_FLAGS(ECX, EAX);
		if(CF == 0 && ZF == 0) { // JA 0x0070398b
			return (Supplier<Object>)this::LAB_0070398b;
		}
		EBX -= ESI;
		return (Supplier<Object>)this::LAB_00703824;
	}

	private Object LAB_00703824() {
		EAX = GET_MEMORY(EBX + ESI, 3);
		SET_MEMORY(EAX, ESI, 3);
		EAX = GET_MEMORY(EBX + ESI + 0x4, 3);
		SET_MEMORY(EAX, ESI + 0x4, 3);
		ESI += 0x8;
		SET_FLAGS(ESI, EDI);
		if(CF == 1) { // JC 0x00703824
			return (Supplier<Object>)this::LAB_00703824;
		}
		ESI = EDI;
		LAB_007022b0();
		ECX = GET_MEMORY(EBP - 0x10, 3);
		EAX = (EAX & 0xffff);
		EDI -= EAX;
		SET_MEMORY(EAX, EBP - 0x4, 3);
		ECX &= 0xf;
		return (Supplier<Object>)this::LAB_0070384c;
	}

	private Object LAB_0070384c() {
		EBX = GET_MEMORY(EBP - 0x18, 3);
		EBX += 0x2;
		SET_MEMORY(EBX, EBP - 0x18, 3);
		SET_FLAGS(ECX, 0xf);
		if(ZF == 0) { // JNZ 0x00703888
			return (Supplier<Object>)this::LAB_00703888;
		}
		EDX = GET_MEMORY(EBP - 0x8, 3);
		EDX -= 0x4;
		ECX = 0;
		return (Supplier<Object>)this::LAB_00703862;
	}

	private Object LAB_00703862() {
		EAX = GET_MEMORY(EBX, 1);
		EBX++;
		ECX += EAX;
		SET_MEMORY(EBX, EBP - 0x18, 3);
		SET_FLAGS(EBX, EDX);
		if(CF == 0) { // JNC 0x007039af
			return (Supplier<Object>)this::LAB_007039af;
		}
		SET_FLAGS(EAX, 0xff);
		if(ZF == 1) { // JZ 0x00703862
			return (Supplier<Object>)this::LAB_00703862;
		}
		ECX += 0xf;
		EAX = ECX + ESI;
		SET_FLAGS(EAX, ESI);
		if(CF == 1) { // JC 0x007039af
			return (Supplier<Object>)this::LAB_007039af;
		}
		return (Supplier<Object>)this::LAB_00703888;
	}

	private Object LAB_00703888() {
		ECX += 0x4;
		SET_FLAGS(EDI, GET_MEMORY(EBP - 0x14, 3));
		if(CF == 1) { // JC 0x007039af
			return (Supplier<Object>)this::LAB_007039af;
		}
		SET_FLAGS(GET_MEMORY(EBP - 0x4, 3), 0x8);
		EDX = ECX + ESI;
		SET_MEMORY(EDX, EBP - 0x28, 3);
		if(CF == 0) { // JNC 0x007038db
			return (Supplier<Object>)this::LAB_007038db;
		}
		SET_MEMORY(0x0, ESI, 3);
		EAX = GET_MEMORY(EDI, 1);
		SET_MEMORY((EAX & 0xff), ESI, 1);
		EAX = GET_MEMORY(EDI + 0x1, 1);
		SET_MEMORY((EAX & 0xff), ESI + 0x1, 1);
		EAX = GET_MEMORY(EDI + 0x2, 1);
		SET_MEMORY((EAX & 0xff), ESI + 0x2, 1);
		EAX = GET_MEMORY(EDI + 0x3, 1);
		SET_MEMORY((EAX & 0xff), ESI + 0x3, 1);
		EAX = GET_MEMORY(EBP - 0x4, 3);
		EDI += INT_00e6cab8[EAX]; //GET_MEMORY(EAX*0x4 + 0xe6cab8, 3);
		
		EAX = GET_MEMORY(EDI, 3);
		SET_MEMORY(EAX, ESI + 0x4, 3);
		EAX = GET_MEMORY(EBP - 0x4, 3);
		EDI -= INT_00e6cbe0[EAX]; //GET_MEMORY(EAX*0x4 + 0xe6cbe0, 3);
		return (Supplier<Object>)this::LAB_007038e8; // JMP 0x007038e8
	}

	private Object LAB_007038db() {
		EAX = GET_MEMORY(EDI, 3);
		SET_MEMORY(EAX, ESI, 3);
		EAX = GET_MEMORY(EDI + 0x4, 3);
		EDI += 0x8;
		SET_MEMORY(EAX, ESI + 0x4, 3);
		return (Supplier<Object>)this::LAB_007038e8;
	}

	private Object LAB_007038e8() {
		EAX = GET_MEMORY(EBP - 0xc, 3);
		ESI += 0x8;
		EAX -= 0xc;
		SET_FLAGS(EDX, EAX);
		if(CF == 1 || ZF == 1) { // JBE 0x00703953
			return (Supplier<Object>)this::LAB_00703953;
		}
		EAX = GET_MEMORY(EBP - 0xc, 3);
		ECX = EAX - 0x7;
		EAX -= 0x5;
		SET_MEMORY(ECX, EBP - 0x10, 3);
		SET_FLAGS(EDX, EAX);
		if(CF == 0 && ZF == 0) { // JA 0x007039af
			return (Supplier<Object>)this::LAB_007039af;
		}
		SET_FLAGS(ESI, ECX);
		if(CF == 0) { // JNC 0x00703937
			return (Supplier<Object>)this::LAB_00703937;
		}
		EBX = GET_MEMORY(EBP - 0x10, 3);
		EDX = EDI;
		ECX = ESI;
		EDX -= ESI;
		return (Supplier<Object>)this::LAB_00703916;
	}

	private Object LAB_00703916() {
		EAX = GET_MEMORY(EDX + ECX, 3);
		SET_MEMORY(EAX, ECX, 3);
		EAX = GET_MEMORY(EDX + ECX + 0x4, 3);
		SET_MEMORY(EAX, ECX + 0x4, 3);
		ECX += 0x8;
		SET_FLAGS(ECX, EBX);
		if(CF == 1) { // JC 0x00703916
			return (Supplier<Object>)this::LAB_00703916;
		}
		EDX = GET_MEMORY(EBP - 0x28, 3);
		EAX = EBX;
		EAX -= ESI;
		ESI = EBX;
		EBX = GET_MEMORY(EBP - 0x18, 3);
		EDI += EAX;
		return (Supplier<Object>)this::LAB_00703937;
	}

	private Object LAB_00703937() {
		SET_FLAGS(ESI, EDX);
		if(CF == 0) { // JNC 0x00703984
			return (Supplier<Object>)this::LAB_00703984;
		}
		return (Supplier<Object>)this::LAB_00703940;
	}

	private Object LAB_00703940() {
		EAX = (EAX & 0xffffff00) | ((GET_MEMORY(EDI, 1)) & 0xff);
		EDI = EDI + 0x1;
		SET_MEMORY((EAX & 0xff), ESI, 1);
		ESI++;
		SET_FLAGS(ESI, EDX);
		if(CF == 1) { // JC 0x00703940
			return (Supplier<Object>)this::LAB_00703940;
		}
		ESI = EDX;
		return (Supplier<Object>)this::LAB_00703724; // JMP 0x00703724
	}

	private Object LAB_00703953() {
		EAX = GET_MEMORY(EDI, 3);
		SET_MEMORY(EAX, ESI, 3);
		EAX = GET_MEMORY(EDI + 0x4, 3);
		SET_MEMORY(EAX, ESI + 0x4, 3);
		SET_FLAGS(ECX, 0x10);
		if(CF == 1 || ZF == 1) { // JBE 0x00703984
			return (Supplier<Object>)this::LAB_00703984;
		}
		ESI += 0x8;
		EDI -= ESI;
		return (Supplier<Object>)this::LAB_00703970;
	}

	private Object LAB_00703970() {
		EAX = GET_MEMORY(EDI + ESI + 0x8, 3);
		SET_MEMORY(EAX, ESI, 3);
		EAX = GET_MEMORY(EDI + ESI + 0xc, 3);
		SET_MEMORY(EAX, ESI + 0x4, 3);
		ESI += 0x8;
		SET_FLAGS(ESI, EDX);
		if(CF == 1) { // JC 0x00703970
			return (Supplier<Object>)this::LAB_00703970;
		}
		return (Supplier<Object>)this::LAB_00703984;
	}

	private Object LAB_00703984() {
		ESI = EDX;
		return (Supplier<Object>)this::LAB_00703724; // JMP 0x00703724
	}

	private Object LAB_0070398b() {
		SET_FLAGS(ECX, GET_MEMORY(EBP - 0x8, 3));
		if(ZF == 0) { // JNZ 0x007039af
			return (Supplier<Object>)this::LAB_007039af;
		}
		SET_FLAGS(EDI, GET_MEMORY(EBP - 0xc, 3));
		if(CF == 0 && ZF == 0) { // JA 0x007039af
			return (Supplier<Object>)this::LAB_007039af;
		}
		PUSH(EDX);
		PUSH(EBX);
		PUSH(ESI);
		LAB_00d20240();
		EAX = GET_MEMORY(EBP - 0x4, 3);
		ESP += 0xc;
		EAX -= GET_MEMORY(EBP - 0x14, 3);
		EAX += ESI;
		EDI = POP();
		ESI = POP();
		EBX = POP();
		ESP = EBP;
		EBP = POP();
		return null;
	}

	private Object LAB_007039af() {
		EAX = GET_MEMORY(EBP - 0x1c, 3);
		EDI = POP();
		EAX -= EBX;
		ESI = POP();
		EAX--;
		EBX = POP();
		ESP = EBP;
		EBP = POP();
		return null;
	}
	
	private static final int[] INT_00e6cab8 = { 0x0, 0x1, 0x2, 0x1, 0x0, 0x4, 0x4, 0x4, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7, 0x1000b, 0x10 };
	private static final int[] INT_00e6cbe0 = { 0x0, 0x0, 0x0, 0xffffffff, 0xfffffffc, 0x1, 0x2, 0x3, 0x6, 0xd, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7 };
	
	// memmove
	private void LAB_00d20240() {
		// PUSH(EDX);
		// PUSH(EBX);
		// PUSH(ESI);
		// void* memmove(void* destination, void* source, size_t num);
		// memmove(ESI, EBX, EDX);
		
		for(int i = EDX - 1; i >= 0; i--) {
			MEMORY[ESI + i] = MEMORY[EBX + i];
		}
	}
}