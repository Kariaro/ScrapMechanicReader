package sm.hardcoded.lua;

import sm.hardcoded.util.Memory;

class LuaDecomp {
	public int FUN_00c75540(Memory _this, Object type, int size, int param_3) {
		
		// undefined __thiscall FUN_00c75540(void * this, LuaSaveDataType* type, undefined4 size, undefined1 param_3);
		//              undefined         AL:1           <RETURN>
		//              void*             ECX:4 (auto)   this
		//              LuaSaveDataType*  Stack[0x4]:4   type
		//              int               Stack[0x8]:4   size
		//              int               Stack[0xc]:4   param_3
		
		/*
		PUSH EBP                                ; 00c75540
		MOV EBP,ESP                             ; 00c75541
		PUSH EBX                                ; 00c75543
		//MOV EBX,dword ptr [EBP + 0xc]           ; 00c75544
		MOV EBX, param_3
		PUSH ESI                                ; 00c75547
		MOV ESI,ECX                             ; 00c75548
		*/
		
		/*
		;Jump short if not zero (ZF=0)
		TEST EBX,EBX                            ; 00c7554a
		JNZ LAB_00c75556                        ; 00c7554c
		*/
		
		if(param_3 == 0) {
			/*
			LAB_00c7554e:
			POP ESI                                 ; 00c7554e
			XOR AL,AL                               ; 00c7554f
			POP EBX                                 ; 00c75551
			POP EBP                                 ; 00c75552
			RET 0xc                                 ; 00c75553 
			*/
			return 0;
		}
		
		// Jump table
		// dst = src    ZF = 1    CF = 0
		// dst < src    ZF = 0    CF = 1
		// dst > src    ZF = 0    CF = 0
		
		// ESI == this
		/*
		LAB_00c75556:
		//MOV ECX,dword ptr [ESI + 0x8]           ; 00c75556
		MOV ECX,size
		//LEA EAX,[ECX + EBX*0x1]                 ; 00c75559
		LEA EAX,[size + param_3]
	
		; Jump short if above (CF=0 and ZF=0)
		CMP EAX,dword ptr [ESI]                 ; 00c7555c
		JA LAB_00c7554e                         ; 00c7555e
		*/
		
		if(size + param_3 > _this.Int()) {
			return 0;
		}
		
		// ECX = size
		// EAX = size + param_3
		// ESI = this
		
		/*
		MOV EAX,ECX                             ; 00c75560
		AND EAX,0x7                             ; 00c75562
		MOV dword ptr [EBP + 0xc],EAX           ; 00c75565
		JNZ LAB_00c75592                        ; 00c75568
		*/
		
		// ESI = this
		// EBX = param_3
		// EAX = size & 7;
		// test = size & 7;
		int test = size & 7;
		
		/*
		TEST BL,0x7                             ; 00c7556a
		JNZ LAB_00c75592                        ; 00c7556d
		*/
		
		if(test == 0 && test == 7) {
			/*
			MOV EAX,EBX                             ; 00c7556f
			SHR ECX,0x3                             ; 00c75571
			ADD ECX,dword ptr [ESI + 0xc]           ; 00c75574
			SHR EAX,0x3                             ; 00c75577
			*/
			// EAX = (param_3 / 8)
			// ECX = (size / 8) + this.Int(0xc);
			
			/*
			PUSH EAX                                ; 00c7557a
			PUSH ECX                                ; 00c7557b
			//PUSH dword ptr [EBP + 0x8]              ; 00c7557c
			PUSH size
			CALL memcpy                             ; 00c7557f
			*/
			
			//memcpy(size, (size / 8) + (size & 7));
			
			/*
			ADD ESP,0xc                             ; 00c75584
			MOV AL,0x1                              ; 00c75587
			ADD dword ptr [ESI + 0x8],EBX           ; 00c75589
			
			POP ESI                                 ; 00c7558c
			POP EBX                                 ; 00c7558d
			POP EBP                                 ; 00c7558e
			RET 0xc                                 ; 00c7558f
			*/
			
			return 1;
		}
		
		/*
		LAB_00c75592:
			PUSH EDI                                ; 00c75592
			MOV EDI,dword ptr [EBP + 0x8]           ; 00c75593
			LEA EAX,[EBX + 0x7]                     ; 00c75596
			SHR EAX,0x3                             ; 00c75599
			PUSH EAX                                ; 00c7559c
			PUSH 0x0                                ; 00c7559d
			PUSH EDI                                ; 00c7559f
			CALL memset                             ; 00c755a0
			MOV DH,byte ptr [EBP + 0x10]            ; 00c755a5
			MOV EAX,0x8                             ; 00c755a8
			ADD ESP,0xc                             ; 00c755ad
			SUB EAX,dword ptr [EBP + 0xc]           ; 00c755b0
			MOV dword ptr [EBP + 0x8],EAX           ; 00c755b3
		
		LAB_00c755b6:
			MOV ECX,dword ptr [ESI + 0x8]           ; 00c755b6
			MOV EAX,dword ptr [ESI + 0xc]           ; 00c755b9
			SHR ECX,0x3                             ; 00c755bc
			MOV DL,byte ptr [ECX + EAX*0x1]         ; 00c755bf
			MOV EAX,dword ptr [EBP + 0xc]           ; 00c755c2
			MOV ECX,EAX                             ; 00c755c5
			SHL DL,CL                               ; 00c755c7
			OR DL,byte ptr [EDI]                    ; 00c755c9
			MOV byte ptr [EDI],DL                   ; 00c755cb
	
			; Jump short if zero (ZF = 1)
			TEST EAX,EAX                            ; 00c755cd
			JZ LAB_00c755ec                         ; 00c755cf
	
			; Jump short if below or equal (CF=1 or ZF=1)
			CMP EBX,dword ptr [EBP + 0x8]           ; 00c755d1
			JBE LAB_00c755ec                        ; 00c755d4
				MOV ECX,dword ptr [ESI + 0x8]           ; 00c755d6
				MOV EAX,dword ptr [ESI + 0xc]           ; 00c755d9
				SHR ECX,0x3                             ; 00c755dc
				MOV AL,byte ptr [ECX + EAX*0x1 + 0x1]   ; 00c755df
				MOV CL,byte ptr [EBP + 0x8]             ; 00c755e3
				SHR AL,CL                               ; 00c755e6
				OR AL,DL                                ; 00c755e8
				MOV byte ptr [EDI],AL                   ; 00c755ea
			LAB_00c755ec:
	
			; Jump short if carry (CF=1)
			CMP EBX,0x8                             ; 00c755ec
			JC LAB_00c755fa                         ; 00c755ef
				SUB EBX,0x8                             ; 00c755f1
				ADD dword ptr [ESI + 0x8],0x8           ; 00c755f4
				JMP LAB_00c75617                        ; 00c755f8
	
			LAB_00c755fa:
			LEA EAX,[EBX + -0x8]                    ; 00c755fa
	
			; Jump short if not sign (SF=0)
			TEST EAX,EAX                            ; 00c755fd
			JNS LAB_00c75610                        ; 00c755ff
				; Jump short if zero (ZF = 1)
				TEST DH,DH                              ; 00c75601
				JZ LAB_00c7560b                         ; 00c75603
					NEG AL                                  ; 00c75605
					MOV CL,AL                               ; 00c75607
					SHR byte ptr [EDI],CL                   ; 00c75609
	
				LAB_00c7560b:
					ADD dword ptr [ESI + 0x8],EBX           ; 00c7560b
					XOR EBX,EBX                         ; 00c75614
					JMP LAB_00c75617                        ; 00c7560e
				
			LAB_00c75610:
				ADD dword ptr [ESI + 0x8],0x8           ; 00c75610
				XOR EBX,EBX                             ; 00c75614
			
			LAB_00c75617:
			INC EDI                             ; 00c75616
			
			; Jump short if not zero (ZF=0)
			TEST EBX,EBX                            ; 00c75617
			JNZ LAB_00c755b6                        ; 00c75619
	
		LAB_00c7561b:
		POP EDI                                 ; 00c7561b
		POP ESI                                 ; 00c7561c
		MOV AL,0x1                              ; 00c7561d
		POP EBX                                 ; 00c7561f
		POP EBP                                 ; 00c75620
		RET 0xc                                 ; 00c75621
		*/
		return 0xc;
	}
}
