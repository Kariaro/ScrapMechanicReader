package sm.hardcoded.tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TestFunction {
	public TestFunction() {
		initSymbols();
	}
	
	public int decompress(byte[] bytes, byte[] input, int uncompressed_size) {
		// Clear pointers
		STACK.set(0).WriteBytes(new byte[1000000]);
		INPUT.set(0).WriteBytes(new byte[1000000]);
		PARAM_2.set(0).WriteBytes(new byte[1000000]);
		
		// Add data
		INPUT.WriteBytes(bytes);
		
		
		long result = Decompress(new Address("007039c0"), uncompressed_size);
		
		PARAM_2.set(0);
		PARAM_2.Bytes(input, 0, uncompressed_size, true);
		
		// System.out.printf("Result: '%08x'\n", result);
		
		return (int)result;
	}
	
	private static final int[] INT_00e6cab8 = { 0x0, 0x1, 0x2, 0x1, 0x0, 0x4, 0x4, 0x4, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7, 0x1000b, 0x10 };
	private static final int[] INT_00e6cbe0 = { 0x0, 0x0, 0x0, 0xffffffff, 0xfffffffc, 0x1, 0x2, 0x3, 0x6, 0xd, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7 };
	
	private final Pointer TEMP = new Pointer(592);
	private final Pointer STACK = new Pointer(1000000);
	private final Pointer INPUT = new Pointer(1000000);
	private final Pointer PARAM_2 = new Pointer(1000000);
	
	static boolean hexString = true;
	static boolean logStuff = false;
	
	private static final String[] REGISTERS = { "RAX", "RCX", "RDX", "RBX", "RSP", "RBP", "RSI", "RDI" };
	private static final String[] EFLAGS = { "OF", /*"DF", "IF", "TF",*/ "SF", "ZF"/*, "AF"*/, "PF", "CF" };
	
	private MemoryRegion MEMORY;
	private LinkedList<Address> call;
	private Map<String, RLong> regs;
	
	private Map<String, Instruction> INSTRUCTIONS;
	private static final String[][] STRING_INSTRUCTIONS = {
		{ "007039c0", "007039c1", "PUSH", "EBP" },
		{ "007039c1", "007039c3", "MOV", "EBP", "ESP" },
		{ "007039c3", "007039c6", "SUB", "ESP", "0x1c" },
		{ "007039c6", "007039c7", "PUSH", "ESI" },
		{ "007039c7", "007039c9", "MOV", "ESI", "EDX" },
		{ "007039c9", "007039cb", "MOV", "EDX", "ECX" },
		{ "007039cb", "007039ce", "MOV", "dword ptr [EBP + -0x14]", "EDX" },
		{ "007039ce", "007039d0", "TEST", "EDX", "EDX" },
		{ "007039d0", "007039d2", "JNZ", "0x007039da" },
		{ "007039d2", "007039d5", "OR", "EAX", "0xffffffff" },
		{ "007039d5", "007039d6", "POP", "ESI" },
		{ "007039d6", "007039d8", "MOV", "ESP", "EBP" },
		{ "007039d8", "007039d9", "POP", "EBP" },
		{ "007039d9", "007039da", "RET" },
		{ "007039da", "007039dd", "MOV", "EAX", "dword ptr [EBP + 0x8]" },
		{ "007039dd", "007039de", "PUSH", "EBX" },
		{ "007039de", "007039e0", "MOV", "EBX", "EDX" },
		{ "007039e0", "007039e1", "PUSH", "EDI" },
		{ "007039e1", "007039e4", "LEA", "ECX", "[ESI + EAX*0x1]" },
		{ "007039e4", "007039e7", "MOV", "dword ptr [EBP + -0xc]", "ECX" },
		{ "007039e7", "007039ea", "LEA", "EDI", "[ECX + -0x1a]" },
		{ "007039ea", "007039ed", "MOV", "dword ptr [EBP + -0x10]", "EDI" },
		{ "007039ed", "007039ef", "TEST", "EAX", "EAX" },
		{ "007039ef", "007039f1", "JNZ", "0x00703a07" },
		{ "007039f1", "007039f3", "CMP", "byte ptr [EDX]", "AL" },
		{ "007039f3", "007039f4", "POP", "EDI" },
		{ "007039f4", "007039f7", "SETZ", "AL" },
		{ "007039f7", "007039f8", "POP", "EBX" },
		{ "007039f8", "007039f9", "POP", "ESI" },
		{ "007039f9", "00703a00", "LEA", "EAX", "[EAX*0x2 + 0xffffffff]" },
		{ "00703a00", "00703a02", "MOV", "ESP", "EBP" },
		{ "00703a02", "00703a03", "POP", "EBP" },
		{ "00703a03", "00703a04", "RET" },
		{ "00703a04", "00703a07", "MOV", "EDI", "dword ptr [EBP + -0x10]" },
		{ "00703a07", "00703a0a", "MOVZX", "ECX", "byte ptr [EBX]" },
		{ "00703a0a", "00703a0b", "INC", "EBX" },
		{ "00703a0b", "00703a0d", "MOV", "EDX", "ECX" },
		{ "00703a0d", "00703a10", "MOV", "dword ptr [EBP + -0x8]", "ECX" },
		{ "00703a10", "00703a13", "SHR", "EDX", "0x4" },
		{ "00703a13", "00703a16", "MOV", "dword ptr [EBP + -0x4]", "EDX" },
		{ "00703a16", "00703a19", "CMP", "EDX", "0x8" },
		{ "00703a19", "00703a1b", "JA", "0x00703a81" },
		{ "00703a1b", "00703a1d", "CMP", "ESI", "EDI" },
		{ "00703a1d", "00703a1f", "JA", "0x00703a9b" },
		{ "00703a1f", "00703a21", "MOV", "EAX", "dword ptr [EBX]" },
		{ "00703a21", "00703a24", "AND", "ECX", "0xf" },
		{ "00703a24", "00703a26", "MOV", "dword ptr [ESI]", "EAX" },
		{ "00703a26", "00703a29", "MOV", "EAX", "dword ptr [EBX + 0x4]" },
		{ "00703a29", "00703a2b", "ADD", "EBX", "EDX" },
		{ "00703a2b", "00703a2e", "MOV", "dword ptr [EBP + -0x8]", "ECX" },
		{ "00703a2e", "00703a30", "MOV", "ECX", "EBX" },
		{ "00703a30", "00703a33", "MOV", "dword ptr [ESI + 0x4]", "EAX" },
		{ "00703a33", "00703a35", "ADD", "ESI", "EDX" },
		{ "00703a35", "00703a38", "MOV", "dword ptr [EBP + -0x18]", "EBX" },
		
		// { "00703a38", "00703a3d", "CALL", "0x007022b0" }, // NOTE
		{ "00703a38", "00703a3d", "MOV", "AX", "word ptr [ECX]" },
		
		{ "00703a3d", "00703a40", "MOV", "ECX", "dword ptr [EBP + -0x8]" },
		{ "00703a40", "00703a42", "MOV", "EDI", "ESI" },
		{ "00703a42", "00703a45", "MOVZX", "EAX", "AX" },
		{ "00703a45", "00703a48", "ADD", "EBX", "0x2" },
		{ "00703a48", "00703a4a", "SUB", "EDI", "EAX" },
		{ "00703a4a", "00703a4d", "MOV", "dword ptr [EBP + -0x4]", "EAX" },
		{ "00703a4d", "00703a50", "CMP", "ECX", "0xf" },
		{ "00703a50", "00703a52", "JZ", "0x00703a7c" },
		{ "00703a52", "00703a55", "CMP", "EAX", "0x8" },
		{ "00703a55", "00703a57", "JC", "0x00703a7c" },
		{ "00703a57", "00703a59", "MOV", "EAX", "dword ptr [EDI]" },
		{ "00703a59", "00703a5b", "MOV", "dword ptr [ESI]", "EAX" },
		{ "00703a5b", "00703a5e", "MOV", "EAX", "dword ptr [EDI + 0x4]" },
		{ "00703a5e", "00703a61", "MOV", "dword ptr [ESI + 0x4]", "EAX" },
		{ "00703a61", "00703a64", "MOV", "EAX", "dword ptr [EDI + 0x8]" },
		{ "00703a64", "00703a67", "MOV", "dword ptr [ESI + 0x8]", "EAX" },
		{ "00703a67", "00703a6a", "MOV", "EAX", "dword ptr [EDI + 0xc]" },
		{ "00703a6a", "00703a6d", "MOV", "dword ptr [ESI + 0xc]", "EAX" },
		{ "00703a6d", "00703a71", "MOV", "AX", "word ptr [EDI + 0x10]" },
		{ "00703a71", "00703a75", "MOV", "word ptr [ESI + 0x10]", "AX" },
		{ "00703a75", "00703a78", "ADD", "ESI", "0x4" },
		{ "00703a78", "00703a7a", "ADD", "ESI", "ECX" },
		{ "00703a7a", "00703a7c", "JMP", "0x00703a04" },
		{ "00703a7c", "00703a7f", "MOV", "EAX", "dword ptr [EBP + -0x18]" },
		{ "00703a7f", "00703a81", "JMP", "0x00703ade" },
		{ "00703a81", "00703a84", "CMP", "EDX", "0xf" },
		{ "00703a84", "00703a86", "JNZ", "0x00703a9b" },
		{ "00703a86", "00703a88", "XOR", "ECX", "ECX" },
		{ "00703a88", "00703a8b", "MOVZX", "EAX", "byte ptr [EBX]" },
		{ "00703a8b", "00703a8c", "INC", "EBX" },
		{ "00703a8c", "00703a8e", "ADD", "ECX", "EAX" },
		{ "00703a8e", "00703a93", "CMP", "EAX", "0xff" },
		{ "00703a93", "00703a95", "JZ", "0x00703a88" },
		{ "00703a95", "00703a98", "LEA", "EDX", "[ECX + 0xf]" },
		{ "00703a98", "00703a9b", "MOV", "dword ptr [EBP + -0x4]", "EDX" },
		{ "00703a9b", "00703a9e", "MOV", "ECX", "dword ptr [EBP + -0xc]" },
		{ "00703a9e", "00703aa1", "LEA", "EDI", "[EDX + ESI*0x1]" },
		{ "00703aa1", "00703aa4", "LEA", "EAX", "[ECX + -0x8]" },
		{ "00703aa4", "00703aa6", "CMP", "EDI", "EAX" },
		{ "00703aa6", "00703aac", "JA", "0x00703bfb" },
		{ "00703aac", "00703aae", "MOV", "ECX", "EBX" },
		{ "00703aae", "00703ab0", "SUB", "ECX", "ESI" },
		{ "00703ab0", "00703ab3", "MOV", "EAX", "dword ptr [ECX + ESI*0x1]" },
		{ "00703ab3", "00703ab5", "MOV", "dword ptr [ESI]", "EAX" },
		{ "00703ab5", "00703ab9", "MOV", "EAX", "dword ptr [ECX + ESI*0x1 + 0x4]" },
		{ "00703ab9", "00703abc", "MOV", "dword ptr [ESI + 0x4]", "EAX" },
		{ "00703abc", "00703abf", "ADD", "ESI", "0x8" },
		{ "00703abf", "00703ac1", "CMP", "ESI", "EDI" },
		{ "00703ac1", "00703ac3", "JC", "0x00703ab0" },
		{ "00703ac3", "00703ac5", "ADD", "EBX", "EDX" },
		{ "00703ac5", "00703ac7", "MOV", "ESI", "EDI" },
		{ "00703ac7", "00703ac9", "MOV", "ECX", "EBX" },

		// { "00703ac9", "00703ace", "CALL", "0x007022b0" }, // NOTE
		{ "00703ac9", "00703ace", "MOV", "AX", "word ptr [ECX]" },
		
		{ "00703ace", "00703ad1", "MOV", "ECX", "dword ptr [EBP + -0x8]" },
		{ "00703ad1", "00703ad4", "MOVZX", "EAX", "AX" },
		{ "00703ad4", "00703ad6", "SUB", "EDI", "EAX" },
		{ "00703ad6", "00703ad9", "MOV", "dword ptr [EBP + -0x4]", "EAX" },
		{ "00703ad9", "00703adc", "AND", "ECX", "0xf" },
		{ "00703adc", "00703ade", "MOV", "EAX", "EBX" },
		{ "00703ade", "00703ae1", "LEA", "EBX", "[EAX + 0x2]" },
		{ "00703ae1", "00703ae4", "MOV", "dword ptr [EBP + -0x8]", "EBX" },
		{ "00703ae4", "00703ae7", "CMP", "ECX", "0xf" },
		{ "00703ae7", "00703ae9", "JNZ", "0x00703b03" },
		{ "00703ae9", "00703aeb", "XOR", "ECX", "ECX" },
		{ "00703aeb", "00703af0", "NOP", "dword ptr [EAX + EAX*0x1]" },
		{ "00703af0", "00703af3", "MOVZX", "EAX", "byte ptr [EBX]" },
		{ "00703af3", "00703af4", "INC", "EBX" },
		{ "00703af4", "00703af6", "ADD", "ECX", "EAX" },
		{ "00703af6", "00703afb", "CMP", "EAX", "0xff" },
		{ "00703afb", "00703afd", "JZ", "0x00703af0" },
		{ "00703afd", "00703b00", "MOV", "dword ptr [EBP + -0x8]", "EBX" },
		{ "00703b00", "00703b03", "ADD", "ECX", "0xf" },
		{ "00703b03", "00703b06", "ADD", "ECX", "0x4" },
		{ "00703b06", "00703b0a", "CMP", "dword ptr [EBP + -0x4]", "0x8" },
		{ "00703b0a", "00703b0d", "LEA", "EDX", "[ECX + ESI*0x1]" },
		{ "00703b0d", "00703b10", "MOV", "dword ptr [EBP + -0x1c]", "EDX" },
		{ "00703b10", "00703b12", "JNC", "0x00703b4d" },
		{ "00703b12", "00703b18", "MOV", "dword ptr [ESI]", "0x0" },
		{ "00703b18", "00703b1b", "MOVZX", "EAX", "byte ptr [EDI]" },
		{ "00703b1b", "00703b1d", "MOV", "byte ptr [ESI]", "AL" },
		{ "00703b1d", "00703b21", "MOVZX", "EAX", "byte ptr [EDI + 0x1]" },
		{ "00703b21", "00703b24", "MOV", "byte ptr [ESI + 0x1]", "AL" },
		{ "00703b24", "00703b28", "MOVZX", "EAX", "byte ptr [EDI + 0x2]" },
		{ "00703b28", "00703b2b", "MOV", "byte ptr [ESI + 0x2]", "AL" },
		{ "00703b2b", "00703b2f", "MOVZX", "EAX", "byte ptr [EDI + 0x3]" },
		{ "00703b2f", "00703b32", "MOV", "byte ptr [ESI + 0x3]", "AL" },
		{ "00703b32", "00703b35", "MOV", "EAX", "dword ptr [EBP + -0x4]" },
		{ "00703b35", "00703b3c", "ADD", "EDI", "dword ptr [EAX*0x4 + 0xe6cab8]" },
		{ "00703b3c", "00703b3e", "MOV", "EAX", "dword ptr [EDI]" },
		{ "00703b3e", "00703b41", "MOV", "dword ptr [ESI + 0x4]", "EAX" },
		{ "00703b41", "00703b44", "MOV", "EAX", "dword ptr [EBP + -0x4]" },
		{ "00703b44", "00703b4b", "SUB", "EDI", "dword ptr [EAX*0x4 + 0xe6cbe0]" },
		{ "00703b4b", "00703b4d", "JMP", "0x00703b5a" },
		{ "00703b4d", "00703b4f", "MOV", "EAX", "dword ptr [EDI]" },
		{ "00703b4f", "00703b51", "MOV", "dword ptr [ESI]", "EAX" },
		{ "00703b51", "00703b54", "MOV", "EAX", "dword ptr [EDI + 0x4]" },
		{ "00703b54", "00703b57", "ADD", "EDI", "0x8" },
		{ "00703b57", "00703b5a", "MOV", "dword ptr [ESI + 0x4]", "EAX" },
		{ "00703b5a", "00703b5d", "MOV", "EAX", "dword ptr [EBP + -0xc]" },
		{ "00703b5d", "00703b60", "ADD", "ESI", "0x8" },
		{ "00703b60", "00703b63", "ADD", "EAX", "-0xc" },
		{ "00703b63", "00703b65", "CMP", "EDX", "EAX" },
		{ "00703b65", "00703b67", "JBE", "0x00703bc8" },
		{ "00703b67", "00703b6a", "MOV", "EAX", "dword ptr [EBP + -0xc]" },
		{ "00703b6a", "00703b6d", "LEA", "ECX", "[EAX + -0x7]" },
		{ "00703b6d", "00703b70", "ADD", "EAX", "-0x5" },
		{ "00703b70", "00703b73", "MOV", "dword ptr [EBP + -0x18]", "ECX" },
		{ "00703b73", "00703b75", "CMP", "EDX", "EAX" },
		{ "00703b75", "00703b7b", "JA", "0x00703c19" },
		{ "00703b7b", "00703b7d", "CMP", "ESI", "ECX" },
		{ "00703b7d", "00703b7f", "JNC", "0x00703bb1" },
		{ "00703b7f", "00703b82", "MOV", "EBX", "dword ptr [EBP + -0x18]" },
		{ "00703b82", "00703b84", "MOV", "EDX", "EDI" },
		{ "00703b84", "00703b86", "MOV", "ECX", "ESI" },
		{ "00703b86", "00703b88", "SUB", "EDX", "ESI" },
		{ "00703b88", "00703b90", "NOP", "dword ptr [EAX + EAX*0x1]" },
		{ "00703b90", "00703b93", "MOV", "EAX", "dword ptr [EDX + ECX*0x1]" },
		{ "00703b93", "00703b95", "MOV", "dword ptr [ECX]", "EAX" },
		{ "00703b95", "00703b99", "MOV", "EAX", "dword ptr [EDX + ECX*0x1 + 0x4]" },
		{ "00703b99", "00703b9c", "MOV", "dword ptr [ECX + 0x4]", "EAX" },
		{ "00703b9c", "00703b9f", "ADD", "ECX", "0x8" },
		{ "00703b9f", "00703ba1", "CMP", "ECX", "EBX" },
		{ "00703ba1", "00703ba3", "JC", "0x00703b90" },
		{ "00703ba3", "00703ba6", "MOV", "EDX", "dword ptr [EBP + -0x1c]" },
		{ "00703ba6", "00703ba8", "MOV", "EAX", "EBX" },
		{ "00703ba8", "00703baa", "SUB", "EAX", "ESI" },
		{ "00703baa", "00703bac", "MOV", "ESI", "EBX" },
		{ "00703bac", "00703baf", "MOV", "EBX", "dword ptr [EBP + -0x8]" },
		{ "00703baf", "00703bb1", "ADD", "EDI", "EAX" },
		{ "00703bb1", "00703bb3", "CMP", "ESI", "EDX" },
		{ "00703bb3", "00703bb5", "JNC", "0x00703bf4" },
		{ "00703bb5", "00703bb7", "MOV", "AL", "byte ptr [EDI]" },
		{ "00703bb7", "00703bba", "LEA", "EDI", "[EDI + 0x1]" },
		{ "00703bba", "00703bbc", "MOV", "byte ptr [ESI]", "AL" },
		{ "00703bbc", "00703bbd", "INC", "ESI" },
		{ "00703bbd", "00703bbf", "CMP", "ESI", "EDX" },
		{ "00703bbf", "00703bc1", "JC", "0x00703bb5" },
		{ "00703bc1", "00703bc3", "MOV", "ESI", "EDX" },
		{ "00703bc3", "00703bc8", "JMP", "0x00703a04" },
		{ "00703bc8", "00703bca", "MOV", "EAX", "dword ptr [EDI]" },
		{ "00703bca", "00703bcc", "MOV", "dword ptr [ESI]", "EAX" },
		{ "00703bcc", "00703bcf", "MOV", "EAX", "dword ptr [EDI + 0x4]" },
		{ "00703bcf", "00703bd2", "MOV", "dword ptr [ESI + 0x4]", "EAX" },
		{ "00703bd2", "00703bd5", "CMP", "ECX", "0x10" },
		{ "00703bd5", "00703bd7", "JBE", "0x00703bf4" },
		{ "00703bd7", "00703bda", "ADD", "ESI", "0x8" },
		{ "00703bda", "00703bdc", "SUB", "EDI", "ESI" },
		{ "00703bdc", "00703be0", "NOP", "dword ptr [EAX]" },
		{ "00703be0", "00703be4", "MOV", "EAX", "dword ptr [EDI + ESI*0x1 + 0x8]" },
		{ "00703be4", "00703be6", "MOV", "dword ptr [ESI]", "EAX" },
		{ "00703be6", "00703bea", "MOV", "EAX", "dword ptr [EDI + ESI*0x1 + 0xc]" },
		{ "00703bea", "00703bed", "MOV", "dword ptr [ESI + 0x4]", "EAX" },
		{ "00703bed", "00703bf0", "ADD", "ESI", "0x8" },
		{ "00703bf0", "00703bf2", "CMP", "ESI", "EDX" },
		{ "00703bf2", "00703bf4", "JC", "0x00703be0" },
		{ "00703bf4", "00703bf6", "MOV", "ESI", "EDX" },
		{ "00703bf6", "00703bfb", "JMP", "0x00703a04" },
		{ "00703bfb", "00703bfd", "CMP", "EDI", "ECX" },
		{ "00703bfd", "00703bff", "JNZ", "0x00703c19" },
		{ "00703bff", "00703c00", "PUSH", "EDX" },
		{ "00703c00", "00703c01", "PUSH", "EBX" },
		{ "00703c01", "00703c02", "PUSH", "ESI" },
		{ "00703c02", "00703c07", "CALL", "0x00d20240" },
		{ "00703c07", "00703c0a", "MOV", "EAX", "dword ptr [EBP + -0x4]" },
		{ "00703c0a", "00703c0d", "ADD", "ESP", "0xc" },
		{ "00703c0d", "00703c10", "SUB", "EAX", "dword ptr [EBP + -0x14]" },
		{ "00703c10", "00703c12", "ADD", "EAX", "EBX" },
		{ "00703c12", "00703c13", "POP", "EDI" },
		{ "00703c13", "00703c14", "POP", "EBX" },
		{ "00703c14", "00703c15", "POP", "ESI" },
		{ "00703c15", "00703c17", "MOV", "ESP", "EBP" },
		{ "00703c17", "00703c18", "POP", "EBP" },
		{ "00703c18", "00703c19", "RET" },
		{ "00703c19", "00703c1c", "MOV", "EAX", "dword ptr [EBP + -0x14]" },
		{ "00703c1c", "00703c1d", "POP", "EDI" },
		{ "00703c1d", "00703c1f", "SUB", "EAX", "EBX" },
		{ "00703c1f", "00703c20", "POP", "EBX" },
		{ "00703c20", "00703c21", "DEC", "EAX" },
		{ "00703c21", "00703c22", "POP", "ESI" },
		{ "00703c22", "00703c24", "MOV", "ESP", "EBP" },
		{ "00703c24", "00703c25", "POP", "EBP" },
		{ "00703c25", "00703c30", "RET" },

		// { "007022b0", "007022b3", "MOV", "AX", "word ptr [ECX]" },
		// { "007022b3", "007022c0", "RET" },
	};
	
	private void initSymbols() {
		regs = new TreeMap<>();
		call = new LinkedList<>();
		INSTRUCTIONS = new HashMap<>();
		
		for(String[] split : STRING_INSTRUCTIONS) {
			Instruction inst = new Instruction(split);
			INSTRUCTIONS.put(split[0], inst);
		}
		
		List<RLong> reg = new ArrayList<>();
		List<RLong> sub = new ArrayList<>();
		for(String s : REGISTERS) {
			reg.add(new RLong(s, 0L));
		}
		
		for(RLong r : reg) {
			if(r.name.endsWith("X")) {
				char mid = r.name.charAt(1);
				sub.add(new RSLong("E" + mid + "X", 0, RSize.DWORD, r));
				sub.add(new RSLong(mid + "X", 0, RSize.WORD, r));
				sub.add(new RSLong(mid + "H", 8, RSize.BYTE, r));
				sub.add(new RSLong(mid + "L", 0, RSize.BYTE, r));
			} else {
				String mid = r.name.substring(1);
				sub.add(new RSLong("E" + mid, 0, RSize.DWORD, r));
				sub.add(new RSLong(mid, 0, RSize.WORD, r));
				sub.add(new RSLong(mid + "L", 0, RSize.BYTE, r));
			}
		}
		
		for(RLong r : reg) regs.put(r.name, r);
		for(RLong r : sub) regs.put(r.name, r);
		for(String s : EFLAGS) regs.put(s, new RFlag(s, false));
		
		for(int i : INT_00e6cab8) TEMP.WriteInt(i).next();
		for(int i : INT_00e6cbe0) TEMP.WriteInt(i).next();
		TEMP.set(0);
		
		MEMORY = new MemoryRegion();
		MEMORY.addRegion("temp"   , TEMP   , 0x00e6cab8, 592);
		MEMORY.addRegion("stack"  , STACK  , 0x70000000, 1000000);
		MEMORY.addRegion("input"  , INPUT  , 0x70000000 + 1000000, 1000000);
		MEMORY.addRegion("param_2", PARAM_2, 0x70000000 + 2000000, 1000000);
	}
	
	public static String getHexString(byte[] bytes, int start, int maxLength, int lineLength) {
		StringBuilder sb = new StringBuilder();
		int a = 1;
		for(int i = start; i < Math.min(bytes.length, start + maxLength); i++) {
			sb.append(String.format("%02x", bytes[i]));
			if((a ++) % lineLength == 0) sb.append('\n');
		}
		
		return sb.toString();
	}
	
	public static String getHexString(Pointer p, int start, int maxLength, int lineLength) {
		return getHexString(p.data(), start, maxLength, lineLength);
	}
	
	private long Decompress(Address entry, int size) {
		// __fastcall
		// EAX:4			int		<RETURN>
		// ECX:4			byte*	input
		// EDX:4			int*	param_2
		// Stack[0x4]:4		int		size
		
		setValue("ESP", 0x70000000 +  500000); // Stack Address
		setValue("ECX", 0x70000000 + 1000000);
		setValue("EDX", 0x70000000 + 2000000);
		
		setExpression("dword ptr [ESP + 0x8]", size);
		
		call.push(null);
		
		int max = 10000000;
		Instruction inst = getInst(entry);
		while(max-- > 0) { // Just keep calculating until it's done...
			inst = executeInstruction(inst);
			if(inst == null) break;
		}
		
		return getRegister("EAX").value();
	}
	
	private Instruction executeInstruction(Instruction inst) {
		if(logStuff) {
			System.out.println(inst.getAddress() + ", " + inst.toString());
			System.out.println("\n------------------------------------------------");
		}
		
		String[] split = inst.getSplit();
		
		String mn = inst.getMnemonicString();
		Instruction next = inst.getNext();
		Address jmpS = null;
		switch(mn) {
			case "PUSH": getRegister(split[0]).push(); break;
			case "POP": getRegister(split[0]).pop(); break;
			case "MOV": MOV(split); break;
			case "SUB": SUB(split); break;
			case "TEST": TEST(split); break;
			case "CMP": CMP(split); break;
			
			case "JMP":
			case "JNZ":
			case "JNC":
			case "JA":
			case "JZ":
			case "JC":
			case "JBE": jmpS = JUMP(mn, split); break;
			
			case "OR": OR(split); break;
			case "RET": next = getInst(call.poll()); break;
			case "LEA": LEA(split); break;
			case "SETZ": SETZ(split); break;
			case "MOVZX": MOVZX(split); break;
			case "INC": getRegister(split[0]).inc(); break;
			case "DEC": getRegister(split[0]).dec(); break;
			case "SHR": SHR(split); break;
			case "AND": AND(split); break;
			case "ADD": getRegister(split[0]).add(getValue(split[1])); break;
			case "CALL": jmpS = CALL(inst, split); break;
			case "NOP": break;
			case "XOR": XOR(split); break;
			
			default: {
				System.err.println("IMPL: " + mn);
				System.err.println("INP: " + arrToStr(split));
				
				return null;
			}
		}
		
		if(logStuff) {
			StringBuilder sb = new StringBuilder();
			for(RLong r : regs.values()) if(r.name.charAt(0) == 'E' && r.name.length() == 3) sb.append(r.name).append("=").append(r.toString()).append(", ");
			sb.append("\n\n");
			for(RLong r : regs.values()) if(r instanceof RFlag) sb.append(r.name).append("=").append(r.toString()).append(", ");
			sb.append("\n\n");
			System.out.println(sb.toString());
		}
		
		if(jmpS != null) return getInst(jmpS);
		return next;
	}
	
	private Address CALL(Instruction curr, String[] split) {
		long offset = getValue(split[0]);
		
		// void * memmove ( void * destination, const void * source, size_t num );
		if(offset == 0x00d20240) { // (C++) memmove
			long idst = getValue("ESI");
			long isrc = getValue("EBX");
			long num = getValue("EDX");
			
			Region rdst = MEMORY.getRegionContaining(idst);
			if(rdst == null) throw new NonImpl();
			idst -= rdst.start;
			
			Region rsrc = MEMORY.getRegionContaining(isrc);
			if(rsrc == null) throw new NonImpl();
			isrc -= rsrc.start;
			
			Pointer dst = rdst.memory();
			Pointer src = rsrc.memory();
			
			dst.push();
			src.push();
			dst.set((int)idst);
			src.set((int)isrc);
			dst.WriteBytes(src.Bytes((int)num));
			src.pop();
			dst.pop();
			
			return curr.getNext().getAddress();
		}
		
		call.push(curr.getNext().getAddress());
		return getAddr(offset);
	}
	
	private void XOR(String[] split) {
		RLong r = getRegister(split[0]);
		r.set(r.value() ^ getValue(split[1]));
		
		setFlag("CF", false);
		setFlag("OF", false);
	}
	
	private void AND(String[] split) {
		RLong r = getRegister(split[0]);
		r.set(r.value() & getValue(split[1]));
	}
	
	private void SHR(String[] split) {
		RLong r = getRegister(split[0]);
		r.set(r.value() >>> getValue(split[1]));
	}
	
	private void MOVZX(String[] split) {
		// MOVZX is the same as this but MOVZX is needed for assembly
		MOV(split);
	}
	
	private void SETZ(String[] split) {
		setValue(split[0], getFlag("ZF"));
	}
	
	private void CMP(String[] split) {
		long a = getValue(split[0]);
		long b = getValue(split[1]);
		long c = a - b;
		
		setFlag("ZF", c == 0);
		setFlag("CF", b > a);
		setFlag("OF", false); // TODO - (OF) flag
	}
	
	private Address JUMP(String mn, String[] split) {
		long ZF = getFlag("ZF");
		long CF = getFlag("CF");
		
		boolean jump = false;
		switch(mn) {
			case "JMP": jump = true; break;
			case "JNZ": jump = (ZF == 0); break;
			case "JZ":  jump = (ZF == 1); break;
			case "JC":  jump = (CF == 1); break;
			case "JNC": jump = (CF == 0); break;
			case "JA":  jump = (CF == 0) && (ZF == 0); break;
			case "JBE": jump = (CF == 1) || (ZF == 1); break;
			default:
				throw new NonImpl();
		}
		
		if(jump) return getAddr(getValue(split[0]));
		return null;
	}
	
	private void MOV(String[] split) {
		long result = getValue(split[1]);
		setValue(split[0], result);
	}
	
	private void LEA(String[] split) {
		long result = getValue(split[1]);
		RType type = getType(split[0]);
		if(type != RType.REGISTER) throw new NonImpl();
		
		setValue(split[0], result);
	}
	
	private void OR(String[] split) {
		RLong reg = getRegister(split[0]);
		
		long a = reg.value();
		long b = getValue(split[1]);
		
		// Cleared
		setFlag("CF", 0);
		setFlag("OF", 0);
		
		reg.set(a | b);
	}
	
	private void TEST(String[] split) {
		long a = getValue(split[0]);
		long b = getValue(split[1]);
		long c = a & b;
		
		setFlag("ZF", c == 0);
		setFlag("OF", false);
		setFlag("SF", c < 0);
	}
	
	private void SUB(String[] split) {
		RLong reg = getRegister(split[0]);
		reg.sub(getValue(split[1]));
	}
	
	private void setFlag(String name, long value) { setFlag(name, !(value == 0)); }
	private void setFlag(String name, boolean value) {
		if(!regs.containsKey(name)) {
			throw new NonImpl();
		}
		
		RLong r = regs.get(name);
		if(!(r instanceof RFlag)) throw new NonImpl();
		
		r.set(value ? 1:0);
	}
	private long getFlag(String name) {
		if(!regs.containsKey(name)) {
			throw new NonImpl();
		}
		
		RLong r = regs.get(name);
		if(!(r instanceof RFlag)) throw new NonImpl();
		
		return r.value();
	}
	private RLong getRegister(String name) {
		if(!regs.containsKey(name)) throw new NonImpl();
		RLong r = regs.get(name);
		if(r instanceof RFlag) throw new NonImpl();
		return r;
	}
	private void setValue(String str, long value) {
		RType type = getType(str);
		
		switch(type) {
			case CONSTANT: throw new NonImpl(); // Illegal
			case REGISTER: getRegister(str).set(value); break;
			case EXPRESSION: setExpression(str, value); break;
		}
	}
	
	private long getValue(String str) {
		RType type = getType(str);
		
		switch(type) {
			case CONSTANT: return getConstantValue(str);
			case REGISTER: return getRegister(str).value();
			case EXPRESSION: return getExpression(str);
		}
		
		return 0L;
	}
	
	private void setExpression(String str, long value) {
		Expr exp = solveExpression(str);
		
		if(exp.ptr) {
			Region r = MEMORY.getRegionContaining(exp.index);
			if(r == null) throw new NonImpl();

			int index = (int)(exp.index - r.start);
			if(logStuff) System.out.println("ptr -> " + exp + ", index = " + String.format("%08x  new = %08x", exp.index, index));
			
			Pointer p = r.memory();
			switch(exp.size) {
				case BYTE: p.WriteByte((int)value, index); break;
				case WORD: p.WriteShort((int)value, index); break;
				case DWORD: p.WriteInt((int)value, index); break;
			}
		} else {
			// Illegal
			throw new NonImpl();
		}
		
		if(logStuff) System.out.println("setExpression -> " + exp);
	}
	
	private long getExpression(String str) {
		Expr exp = solveExpression(str);
		if(logStuff) System.out.println("getExpression -> " + exp);
		
		if(exp.ptr) {
			Region r = MEMORY.getRegionContaining(exp.index);
			if(r == null) throw new NonImpl();
			
			int index = (int)(exp.index - r.start);
			if(logStuff) System.out.println("ptr -> " + exp + ", index = " + String.format("%08x  new = %08x", exp.index, index));
			
			long var = 0;
			Pointer p = r.memory();
			switch(exp.size) {
				case BYTE: var = p.UnsignedByte(index); break;
				case WORD: var = p.UnsignedShort(index); break;
				case DWORD: var = p.UnsignedInt(index); break;
				default: throw new NonImpl();
			}
			
			if(logStuff) System.out.printf("ptr -> v:[%08x]  value = '%d'\n", var, var);
			
			return var;
		} else {
			return exp.index;
		}
	}
	
	private Expr solveExpression(String string) {
		Expr exp = new Expr();
		
		int ptr_index = string.indexOf("ptr");
		if(ptr_index != -1) {
			
			String size = string.substring(0, string.indexOf(' '));
			exp.size = RSize.valueOf(size.toUpperCase());
			exp.ptr = true;
			
			string = string.substring(ptr_index + 4);
			exp.expression = string;
		} else {
			exp.expression = string;
		}
		
		// Combine all multiplications and separate all additions and subtractions
		string = '+' + string.substring(1, string.length() - 1);
		string = string.replaceAll("[ ]+\\*[ ]+", "*"); // combine mul
		string = string.replace("+", " + ").replace("-", " - ");
		
		String[] arr = string.split("[ ]+");
		
		long index = 0;
		boolean add = false;
		boolean sub = false;
		for(String s : arr) {
			if(s.trim().isEmpty()) continue;
			
			if(s.equals("+")) {
				add = true; sub = false;
				continue;
			}
			
			if(s.equals("-")) {
				add = false; sub = true;
				continue;
			}
			
			boolean mul = s.indexOf("*") != -1;
			
			long v = 0;
			if(mul) {
				String[] segs = s.split("\\*");
				v = getValue(segs[0]);
				
				for(int i = 1; i < segs.length; i++) v *= getValue(segs[i]);
			} else v = getValue(s);
			
			if(add) index += v;
			if(sub) index -= v;
			
			// System.out.printf("%s = '%08x' (%d)\n", s, v, v);
		}
		exp.index = index;
		
		return exp;
	}
	
	private Instruction getInst(Address addr) {
		if(addr == null) return null;
		//return getInstructionAt(addr);
		return INSTRUCTIONS.get(addr.toString());
	}
	
	private long getConstantValue(String str) {
		boolean neg = false;
		if(str.startsWith("-")) {
			neg = true;
			str = str.substring(1);
		}
		
		if(str.startsWith("0x")) {
			str = str.substring(2);
			return Long.valueOf(str, 16) * (neg ? -1:1);
		}
		
		return Long.valueOf(str) * (neg ? -1:1);
	}
	
	private Address getAddr(long l) {
		//return getAddressFactory().getAddress(Long.toHexString(l)); }
		return new Address(l);
	}
	
	private RType getType(String str) {
		if(str.indexOf('[') > -1 || str.indexOf(']') > -1) return RType.EXPRESSION;
		if(str.startsWith("-0x") || str.startsWith("0x")) return RType.CONSTANT;
		return RType.REGISTER;
	}
	
	private String arrToStr(String[] arr) {
		StringBuilder sb = new StringBuilder().append("[ ");
		for(String s : arr) sb.append(s).append(", ");
		if(arr.length > 0) sb.deleteCharAt(sb.length() - 2);
		return sb.append("]").toString();
	}
	
	private enum RSize { BYTE, WORD, DWORD }
	private enum RType { EXPRESSION, CONSTANT, REGISTER }
	
	private class Expr {
		public boolean ptr;
		
		public RSize size;
		public String expression;
		public long index;
		
		@Override
		public String toString() {
			String str = "Expr '" + expression + "' index=" + index;
			
			if(ptr) str += " [Pointer " + size + "]";
			
			return str;
		}
	}
	
	class RFlag extends RLong {
		public RFlag(String name, boolean set) {
			super(name, set ? 1:0);
		}
		
		public void pop() { throw new NonImpl(); }
		public void push() { throw new NonImpl(); }
		public long value() { return super.value() == 0 ? 0:1; }
		public void set(long value) { super.set(value == 0 ? 0:1); }
		public String toString() { return Long.toString(value()); }
	}
	
	class RSLong extends RLong {
		private final long offset;
		private final RSize size;
		private final RLong reg;
		
		private RSLong(String name, int offset, RSize size, RLong reg) {
			super(name, 0);
			this.reg = reg;
			this.offset = offset;
			this.size = size;
		}
		
		public void set(long value) {
			super.set(value);
			long byte_size = ((long)Math.pow(2, size.ordinal())) * 8L;
			
			//  byte -  8
			//  word - 16
			// dword - 32
			long mask = (1L << (byte_size)) - 1;
			long vals = value & mask;
			
			vals <<= offset;
			mask <<= offset;
			long parent = reg.value();
			parent &= ~mask;
			parent |= vals;
			reg.set(parent);
		}
		
		public long value() {
			long byte_size = ((long)Math.pow(2, size.ordinal())) * 8L;
			long mask = ((1L << (byte_size)) - 1) << offset;
			long val = (reg.value() & mask) >>> offset;
			
			switch(size) {
				case BYTE: return (byte)val;
				case WORD: return (short)val;
				case DWORD: return (int)val;
			}
			
			return val;
		}
		
		public void push() {
			if(size == RSize.BYTE) throw new NonImpl();
			super.push();
		}
		
		public void pop() {
			if(size == RSize.BYTE) throw new NonImpl();
			super.pop();
			set(super.value());
		}
		
		@Override
		public String toString() {
			long val = value();
			
			if(hexString) {
				switch(size) {
					case BYTE: return String.format("%02x", (byte)value());
					case WORD: return String.format("%04x", (short)value());
					case DWORD: return String.format("%08x", (int)value());
				}
			} else {
				switch(size) {
					case BYTE: return Byte.toString((byte)value());
					case WORD: return Short.toString((short)value());
					case DWORD: return Integer.toString((int)value());
				}
			}
			
			
			return Long.toUnsignedString(val);
		}
	}
	
	class RLong {
		public final String name;
		protected LinkedList<Long> list;
		
		public RLong(String name, long value) {
			this.name = name;
			this.list = new LinkedList<>();
			this.list.add(value);
		}
		
		public void push() { list.addLast(value()); }
		public void pop() { list.removeLast(); }
		public long value() { return list.getLast(); }
		public void set(long value) { list.set(list.size() - 1, value); }
		
		public void sub(long value) { set(value() - value); }
		public void add(long value) { set(value() + value); }
		public void mul(long value) { set(value() * value); }
		public void inc() { add(1); }
		public void dec() { sub(1); }
		
		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		/*
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof String) {
				String val = obj.toString();
				
				char f = val.charAt(0);
				if(f == 'R') return name.equals(val);
				if(f == 'E') return name.substring(1).equals(val.substring(1));
				if(val.length() == 2) {
					if(name.substring(1).equals(val)) return true;
					if(name.charAt(1) == f) return true;
				}
				
				if(val.length() == 3) {
					return name.substring(1).equals(val.substring(0, 2));
				}
				
				return false;
			}
			if(obj instanceof Long) {
				return ((Long)obj).longValue() == value();
			}
			if(obj instanceof RLong) {
				return ((RLong)obj).value() == value();
			}
			return false;
		}
		*/
		
		public String toString() {
			if(hexString) return String.format("%016x", value());
			return Long.toString(value());
		}
	}
	
	private class NonImpl extends Error {
		private static final long serialVersionUID = -4539765818115105110L;
	}
	
	class Region {
		private String name;
		private Pointer ptr;
		private long start;
		private long size;
		
		public Region(String name, Pointer ptr, long start, long size) {
			this.name = name;
			this.start = start;
			this.size = size;
			this.ptr = ptr;
		}
		
		public long min() { return start; }
		public long max() { return start + size; }
		
		public boolean contains(long address) {
			return min() <= address && max() > address;
		}
		public boolean contains(Region r) {
			return min() <= r.max() && max() > r.min();
		}
		
		public Pointer memory() { return ptr; }
		public String toString() { return name; }
	}
	
	class MemoryRegion {
		private final List<Region> regions;
		
		public MemoryRegion() {
			regions = new ArrayList<>();
		}
		
		public void addRegion(String name, Pointer ptr, long start, long size) {
			regions.add(new Region(name, ptr, start, size));
		}
		
		public Region getRegionContaining(long address) {
			for(Region r : regions) {
				if(r.contains(address)) return r;
			}
			return null;
		}
	}
	
	class Address {
		private long index;
		
		public Address(String address) {
			this(Long.valueOf(address, 16));
		}
		
		public Address(long offset) {
			this.index = offset;
		}
		
		public long offset() {
			return index;
		}
		
		public String toString() {
			return String.format("%08x", index);
		}
	}
	
	class Instruction {
		private final Address address;
		private final Address next;
		private final String mnemonic;
		private final int operands;
		private final String[] data;
		private final String[] split;
		public Instruction(String[] split) {
			data = split;
			address = new Address(split[0]);
			next = new Address(split[1]);
			mnemonic = split[2];
			operands = split.length - 3;
			
			this.split = new String[operands];
			System.arraycopy(split, 3, this.split, 0, operands);
		}
		
		public String getMnemonicString() { return mnemonic; }
		public int getNumOperands() { return operands; }
		public Instruction getNext() { return getInst(next); }
		public Address getAddress() { return address; }
		public String getDefaultOperandRepresentation(int i) { return data[i + 3]; }
		public String[] getSplit() { return split; }
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(mnemonic).append(" ");
			for(int i = 0; i < operands; i++) sb.append(getDefaultOperandRepresentation(i)).append(", ");
			
			if(operands > 0) {
				sb.deleteCharAt(sb.length() - 1);
				sb.deleteCharAt(sb.length() - 1);
			}
			
			return sb.toString();
		}
	}
}
