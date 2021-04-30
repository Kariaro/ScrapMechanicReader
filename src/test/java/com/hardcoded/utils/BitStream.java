package com.hardcoded.utils;

public class BitStream {
	// typedef uint32_t BitSize_t
	
	/*  0x0 */ int numberOfBitsUsed;
	/*  0x4 */ int numberOfBitsAllocated;
	/*  0x8 */ int readOffset;
	/*  0xC */ char[] data;
	/* 0x10 */ boolean copyData;
	/* 0x11 */ char[] stackData = new char[256];
	
	// void BitStream::WriteBits( const unsigned char* inByteArray, BitSize_t numberOfBitsToWrite, const bool rightAlignedBits );
	void WriteBits(char[] inByteArray, int numberOfBitsToWrite, boolean rightAlignedBits) {
		AddBitsAndReallocate(numberOfBitsToWrite);
		
		int numberOfBitsUsedMod8 = numberOfBitsUsed & 7;
		if(numberOfBitsUsedMod8 == 0 && (numberOfBitsToWrite & 7) == 0) {
			Cpp.memcpy(data, numberOfBitsUsed >> 3, inByteArray, 0, numberOfBitsToWrite >> 3);
			numberOfBitsUsed += numberOfBitsToWrite;
			return;
		}
		
		char dataByte;
		int inputPtr = 0;
		
		while(numberOfBitsToWrite > 0) {
			dataByte = inByteArray[inputPtr++];
			
			if(numberOfBitsToWrite < 8 && rightAlignedBits) {
				dataByte <<= 8 - numberOfBitsToWrite;
			}
			
			if(numberOfBitsUsedMod8 == 0) {
				data[numberOfBitsUsed >> 3] = dataByte;
			} else {
				data[numberOfBitsUsed >> 3] |= dataByte >> numberOfBitsUsedMod8;
				
				int tmp = 8 - numberOfBitsUsedMod8;
				if(tmp < 8 && tmp < numberOfBitsToWrite) {
					data[(numberOfBitsUsed >> 3) + 1] = (char)(dataByte << tmp);
				}
			}
			
			if(numberOfBitsToWrite >= 8) {
				numberOfBitsUsed += 8;
				numberOfBitsToWrite -= 8;
			} else {
				numberOfBitsUsed += numberOfBitsToWrite;
				numberOfBitsToWrite = 0;
			}
		}
	}
	
	// void BitStream::AddBitsAndReallocate( const BitSize_t numberOfBitsToWrite )
	void AddBitsAndReallocate(int numberOfBitsToWrite) {
		// ???
	}
}
