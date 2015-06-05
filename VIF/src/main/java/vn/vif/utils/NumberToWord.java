package vn.vif.utils;

public class NumberToWord {
	private String[] ChuSo = new String[] { " không", " một", " hai", " ba",
			" bốn", " năm", " sáu", " bảy", " tám", " chín" };
	private String[] Tien = new String[] { "", " nghìn", " triệu", " tỷ",
			" nghìn tỷ", " triệu tỷ", " tỷ tỷ" };

	public String read(double SoTien) {
		return read((long)SoTien);
	}
	// Hàm đọc số thành chữ
	public String read(long SoTien) {
		int lan = 0, i;
		long so;
		String KetQua = "", tmp = "";
		long[] ViTri = new long[Tien.length];
		if (SoTien < 0)
			return "Số tiền âm";
		if (SoTien == 0)
			return "Không đồng";
		if (SoTien > 0) {
			so = SoTien;
		} else {
			so = -SoTien;
		}
		String num = String.valueOf(so);
		for (int j = 0; j < ViTri.length; j++) {
			if (num.length() <= 3) {
				ViTri[j] = Long.parseLong(num);
				break;
			} else {
				ViTri[j] = Long.parseLong(num.substring(num.length() - 3));
				num = num.substring(0, num.length() - 3);
			}
		}
		for (int j = ViTri.length - 1; j >= 0; j--) {
			if (ViTri[j] > 0) {
				lan = j;
				break;
			}
		}
		for (i = lan; i >= 0; i--) {
			tmp = DocSo3ChuSo((int) ViTri[i]);
			KetQua += tmp;
			if (ViTri[i] != 0)
				KetQua += Tien[i];
			if ((i > 0) && (!tmp.isEmpty()))
				KetQua += ",";// && (!string.IsNullOrEmpty(tmp))
		}
		if (KetQua.endsWith(","))
			KetQua = KetQua.substring(0, KetQua.length() - 1);
		KetQua = KetQua.trim();
		return KetQua.substring(0, 1).toUpperCase() + KetQua.substring(1);
	}

	// Hàm đọc số có 3 chữ số
	private String DocSo3ChuSo(int baso) {
		int tram, chuc, donvi;
		String KetQua = "";
		tram = (int) (baso / 100);
		chuc = (int) ((baso % 100) / 10);
		donvi = baso % 10;
		if ((tram == 0) && (chuc == 0) && (donvi == 0))
			return "";
		if (tram != 0) {
			KetQua += ChuSo[tram] + " trăm";
			if ((chuc == 0) && (donvi != 0))
				KetQua += " linh";
		}
		if ((chuc != 0) && (chuc != 1)) {
			KetQua += ChuSo[chuc] + " mươi";
			if ((chuc == 0) && (donvi != 0))
				KetQua = KetQua + " linh";
		}
		if (chuc == 1)
			KetQua += " mười";
		switch (donvi) {
		case 1:
			if ((chuc != 0) && (chuc != 1)) {
				KetQua += " mốt";
			} else {
				KetQua += ChuSo[donvi];
			}
			break;
		case 5:
			if (chuc == 0) {
				KetQua += ChuSo[donvi];
			} else {
				KetQua += " lăm";
			}
			break;
		default:
			if (donvi != 0) {
				KetQua += ChuSo[donvi];
			}
			break;
		}
		return KetQua;
	}
	
	public static void main(String[] args) {
		System.out.println(new NumberToWord().read(5333356323336083123l));
	}
}
